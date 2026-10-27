package com.micro.order.dto;

import com.micro.order.model.OrderStatus;
import com.micro.order.dto.OrderItemResponse;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderResponse {
    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus orderStatus;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}