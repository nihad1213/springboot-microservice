package com.micro.SpringMicro.controller;

import com.micro.SpringMicro.dto.OrderResponse;
import com.micro.SpringMicro.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(@RequestHeader("X-User-ID") Long userId) {
        OrderResponse createdOrder = orderService.createOrder(userId);
        return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
    }
}