package com.micro.order.service;

import com.micro.order.dto.OrderItemResponse;
import com.micro.order.dto.OrderResponse;
import com.micro.order.model.*;
import com.micro.order.repository.CartItemRepository;
import com.micro.order.repository.OrderRepository;
import com.micro.order.dto.OrderItem;
import com.micro.order.model.Order;
import com.micro.order.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final CartItemRepository cartItemRepository;
//    private final UserRepository userRepository;
//    private final ProductRepository productRepository;

    @Transactional
    public OrderResponse createOrder(Long userId) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
//
//        List<CartItem> cartItems = cartItemRepository.findByUserId(userId);
//        if (cartItems.isEmpty()) {
//            throw new RuntimeException("Cart is empty. Cannot place an order.");
//        }
//
//        Order order = new Order();
//        order.setUser(user);
//        order.setOrderStatus(OrderStatus.CONFIRMED);
//
//        BigDecimal totalAmount = BigDecimal.ZERO;
//
//        for (CartItem cartItem : cartItems) {
//            Product product = cartItem.getProduct();
//
//            if (product.getQuantity() == null || product.getQuantity() < cartItem.getQuantity()) {
//                throw new RuntimeException("Anbarda kifayət qədər məhsul yoxdur: " + product.getName() +
//                        " (Mövcud stok: " + (product.getQuantity() != null ? product.getQuantity() : 0) + ")");
//            }
//
//            product.setQuantity(product.getQuantity() - cartItem.getQuantity());
//            productRepository.save(product);
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setProduct(product);
//            orderItem.setQuantity(cartItem.getQuantity());
//            orderItem.setPrice(cartItem.getPrice());
//
//            order.addOrderItem(orderItem);
//
//            if (cartItem.getPrice() != null) {
//                totalAmount = totalAmount.add(cartItem.getPrice());
//            }
//        }
//
//        order.setTotalAmount(totalAmount);
//
//        Order savedOrder = orderRepository.save(order);
//
//        cartItemRepository.deleteAll(cartItems);
//
//        return mapToOrderResponse(savedOrder);

        return null;
    }

//    private OrderResponse mapToOrderResponse(Order order) {
//        OrderResponse response = new OrderResponse();
//        response.setId(order.getId());
//        response.setTotalAmount(order.getTotalAmount());
//        response.setOrderStatus(order.getOrderStatus());
//        response.setCreatedAt(order.getCreatedAt());
//
//        List<OrderItemResponse> itemResponses = order.getItems().stream().map(item -> {
//            OrderItemResponse itemRes = new OrderItemResponse();
//            itemRes.setId(item.getId());
//            itemRes.setQuantity(item.getQuantity());
//            itemRes.setPrice(item.getPrice());
//
//            if (item.getProduct() != null) {
//                ProductResponse prodRes = new ProductResponse();
//                prodRes.setId(item.getProduct().getId());
//                prodRes.setName(item.getProduct().getName());
//                prodRes.setDescription(item.getProduct().getDescription());
//                prodRes.setPrice(item.getProduct().getPrice());
//                prodRes.setStockQuantity(item.getProduct().getQuantity());
//                prodRes.setCategory(item.getProduct().getCategory());
//                prodRes.setImageUrl(item.getProduct().getImageUrl());
//                itemRes.setProduct(prodRes);
//            }
//            return itemRes;
//        }).collect(Collectors.toList());
//
//        response.setItems(itemResponses);
//        return response;
//    }
}