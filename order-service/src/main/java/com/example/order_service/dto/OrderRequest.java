package com.example.order_service.dto;

import com.example.order_service.enums.Status;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderRequest {
    private Status status;
    private Double totalAmount;
    private String orderId;
    private String userId;
    private LocalDateTime createdAt;
}