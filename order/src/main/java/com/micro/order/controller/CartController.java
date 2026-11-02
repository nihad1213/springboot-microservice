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
    public ResponseEntity<CartItemResponse> addToCart(@RequestHeader("X-User-ID") String userId,
                                                      @RequestBody CartRequest cartRequest) {
        return ResponseEntity.ok(cartService.addToCart(userId, cartRequest));
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCartItems(@RequestHeader("X-User-ID") String userId) {
        return ResponseEntity.ok(cartService.getCartItems(userId));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteCartItem(@RequestHeader("X-User-ID") String userId,
                                               @PathVariable Long id) {
        cartService.deleteCartItem(userId, id);
        return ResponseEntity.noContent().build();
    }
}
