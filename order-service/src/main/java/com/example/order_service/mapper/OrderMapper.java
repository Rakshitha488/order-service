package com.example.order_service.mapper;

import com.example.order_service.dto.OrderRequest;
import com.example.order_service.model.Order;

import java.time.LocalDateTime;

public class OrderMapper {
    public static OrderRequest convertToDTO(Order order) {

                return OrderRequest.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .status(order.getStatus())
                .currency(order.getCurrency())
                .totalAmount(order.getTotalAmount())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();


    }
}
