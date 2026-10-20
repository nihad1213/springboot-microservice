package com.micro.SpringMicro.service;

import com.micro.SpringMicro.dto.CartItemResponse;
import com.micro.SpringMicro.dto.CartRequest;
import com.micro.SpringMicro.dto.ProductResponse;
import com.micro.SpringMicro.model.CartItem;
import com.micro.SpringMicro.model.Product;
import com.micro.SpringMicro.model.User;
import com.micro.SpringMicro.repository.CartItemRepository;
import com.micro.SpringMicro.repository.ProductRepository;
import com.micro.SpringMicro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartItemResponse addToCart(Long userId, CartRequest cartRequest) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(cartRequest.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + cartRequest.getProductId()));

        CartItem cartItem = cartItemRepository.findByUserIdAndProductId(userId, cartRequest.getProductId())
                .orElse(new CartItem());

        if (cartItem.getId() == null) {
            cartItem.setUser(user);
            cartItem.setProduct(product);
            cartItem.setQuantity(cartRequest.getQuantity());
            BigDecimal productPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
            cartItem.setPrice(productPrice.multiply(BigDecimal.valueOf(cartRequest.getQuantity())));
        } else {
            int newQuantity = cartItem.getQuantity() + cartRequest.getQuantity();
            cartItem.setQuantity(newQuantity);
            BigDecimal productPrice = product.getPrice() != null ? product.getPrice() : BigDecimal.ZERO;
            cartItem.setPrice(productPrice.multiply(BigDecimal.valueOf(newQuantity)));
        }

        CartItem savedCartItem = cartItemRepository.save(cartItem);
        return mapToCartItemResponse(savedCartItem);
    }

    public List<CartItemResponse> getCartItems(Long userId) {
        return cartItemRepository.findByUserId(userId).stream()
                .map(this::mapToCartItemResponse)
                .collect(Collectors.toList());
    }

    public void deleteCartItem(Long id) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + id));
        cartItemRepository.delete(cartItem);
    }

    private CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setQuantity(cartItem.getQuantity());
        response.setPrice(cartItem.getPrice());

        if (cartItem.getProduct() != null) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setId(cartItem.getProduct().getId());
            productResponse.setName(cartItem.getProduct().getName());
            productResponse.setDescription(cartItem.getProduct().getDescription());
            productResponse.setPrice(cartItem.getProduct().getPrice());
            productResponse.setStockQuantity(cartItem.getProduct().getQuantity());
            productResponse.setCategory(cartItem.getProduct().getCategory());
            productResponse.setImageUrl(cartItem.getProduct().getImageUrl());
            response.setProduct(productResponse);
        }

        return response;
    }
}