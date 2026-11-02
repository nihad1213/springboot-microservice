package com.micro.order.service;

import com.micro.order.client.ProductClient;
import com.micro.order.client.UserClient;
import com.micro.order.dto.OrderItemResponse;
import com.micro.order.dto.OrderResponse;
import com.micro.order.dto.ProductResponse;
import com.micro.order.exception.BadRequestException;
import com.micro.order.exception.ResourceNotFoundException;
import com.micro.order.model.CartItem;
import com.micro.order.model.Order;
import com.micro.order.model.OrderItem;
import com.micro.order.model.OrderStatus;
import com.micro.order.repository.CartItemRepository;
import com.micro.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    @Transactional
    public OrderResponse createOrder(String userId) {
        userClient.assertUserExists(userId);

        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            throw new BadRequestException("Cart is empty. Cannot place an order.");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderStatus(OrderStatus.CONFIRMED);

        List<CartItem> reserved = new ArrayList<>();
        try {
            BigDecimal totalAmount = BigDecimal.ZERO;

            for (CartItem cartItem : cartItems) {
                ProductResponse product = productClient.getProduct(cartItem.getProductId());

                productClient.reserveStock(cartItem.getProductId(), cartItem.getQuantity());
                reserved.add(cartItem);

                BigDecimal unitPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
                BigDecimal linePrice = unitPrice.multiply(BigDecimal.valueOf(cartItem.getQuantity()));

                OrderItem orderItem = new OrderItem();
                orderItem.setProductId(cartItem.getProductId());
                orderItem.setProductName(product.getName());
                orderItem.setQuantity(cartItem.getQuantity());
                orderItem.setPrice(linePrice);
                order.addOrderItem(orderItem);

                totalAmount = totalAmount.add(linePrice);
            }

            order.setTotalAmount(totalAmount);
            Order savedOrder = orderRepository.saveAndFlush(order);
            cartItemRepository.deleteAll(cartItems);

            return mapToOrderResponse(savedOrder);
        } catch (RuntimeException e) {
            releaseReservedStock(reserved);
            throw e;
        }
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrders(String userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(this::mapToOrderResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrder(String userId, Long orderId) {
        return orderRepository.findById(orderId)
                .filter(order -> order.getUserId().equals(userId))
                .map(this::mapToOrderResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id: " + orderId));
    }

    private void releaseReservedStock(List<CartItem> reserved) {
        for (CartItem item : reserved) {
            try {
                productClient.releaseStock(item.getProductId(), item.getQuantity());
            } catch (RuntimeException ex) {
                log.error("Failed to release {} x product {} after a failed checkout",
                        item.getQuantity(), item.getProductId(), ex);
            }
        }
    }

    private OrderResponse mapToOrderResponse(Order order) {
        OrderResponse response = new OrderResponse();
        response.setId(order.getId());
        response.setTotalAmount(order.getTotalAmount());
        response.setOrderStatus(order.getOrderStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setItems(order.getItems().stream().map(item -> {
            OrderItemResponse itemResponse = new OrderItemResponse();
            itemResponse.setId(item.getId());
            itemResponse.setProductId(item.getProductId());
            itemResponse.setProductName(item.getProductName());
            itemResponse.setQuantity(item.getQuantity());
            itemResponse.setPrice(item.getPrice());
            return itemResponse;
        }).toList());
        return response;
    }
}