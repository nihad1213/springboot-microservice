package com.micro.SpringMicro.dto;

import com.micro.SpringMicro.model.OrderStatus;
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