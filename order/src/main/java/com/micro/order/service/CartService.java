package com.micro.order.service;

import com.micro.order.client.ProductClient;
import com.micro.order.client.UserClient;
import com.micro.order.dto.CartItemResponse;
import com.micro.order.dto.CartRequest;
import com.micro.order.dto.ProductResponse;
import com.micro.order.exception.BadRequestException;
import com.micro.order.exception.InsufficientStockException;
import com.micro.order.exception.ResourceNotFoundException;
import com.micro.order.model.CartItem;
import com.micro.order.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserClient userClient;
    private final ProductClient productClient;

    @Transactional
    public CartItemResponse addToCart(String userId, CartRequest cartRequest) {
        if (cartRequest.getProductId() == null) {
            throw new BadRequestException("productId is required");
        }
        if (cartRequest.getQuantity() == null || cartRequest.getQuantity() <= 0) {
            throw new BadRequestException("quantity must be greater than 0");
        }

        userClient.assertUserExists(userId);
        ProductResponse product = productClient.getProduct(cartRequest.getProductId());

        CartItem cartItem = cartItemRepository
                .findByUserIdAndProductId(userId, cartRequest.getProductId())
                .orElseGet(() -> {
                    CartItem created = new CartItem();
                    created.setUserId(userId);
                    created.setProductId(cartRequest.getProductId());
                    created.setQuantity(0);
                    return created;
                });

        int newQuantity = cartItem.getQuantity() + cartRequest.getQuantity();
        if (product.getStockQuantity() == null || product.getStockQuantity() < newQuantity) {
            throw new InsufficientStockException("Not enough stock for product " + product.getName()
                    + " (available: " + (product.getStockQuantity() != null ? product.getStockQuantity() : 0) + ")");
        }

        BigDecimal unitPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
        cartItem.setQuantity(newQuantity);
        cartItem.setPrice(unitPrice.multiply(BigDecimal.valueOf(newQuantity)));

        return mapToCartItemResponse(cartItemRepository.save(cartItem), product);
    }

    @Transactional(readOnly = true)
    public List<CartItemResponse> getCartItems(String userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(item -> mapToCartItemResponse(item, fetchProductOrNull(item.getProductId())))
                .toList();
    }

    @Transactional
    public void deleteCartItem(String userId, Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .filter(item -> item.getUserId().equals(userId))
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + id));
        cartItemRepository.delete(cartItem);
    }

    private ProductResponse fetchProductOrNull(Long productId) {
        try {
            return productClient.getProduct(productId);
        } catch (ResourceNotFoundException e) {
            return null;
        }
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem, ProductResponse product) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(cartItem.getPrice());
        response.setProduct(product);
        return response;
    }
}