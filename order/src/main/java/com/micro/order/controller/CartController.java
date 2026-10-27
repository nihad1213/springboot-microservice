package com.micro.order.controller;

import com.micro.order.dto.CartItemResponse;
import com.micro.order.dto.CartRequest;
import com.micro.order.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;

    @PostMapping
    public ResponseEntity<CartItemResponse> addToCart(@RequestBody CartRequest cartRequest) {
        Long currentUserId = 1L;
        return ResponseEntity.ok(cartService.addToCart(currentUserId, cartRequest));
    }

//    @GetMapping
//    public ResponseEntity<List<CartItemResponse>> getCartItems() {
//        Long currentUserId = 1L;
//        return ResponseEntity.ok(cartService.getCartItems(currentUserId));
//    }
//
//    @DeleteMapping("/items/{id}")
//    public ResponseEntity<Void> deleteCartItem(@PathVariable Long id) {
//        cartService.deleteCartItem(id);
//        return ResponseEntity.noContent().build();
//    }
}