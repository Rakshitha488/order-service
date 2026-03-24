package com.example.order_service.dto;

import com.example.order_service.enums.Currency;
import com.example.order_service.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderRequest {
    private Status status;
    private Double totalAmount;
    private String orderId;
    private String userId;
    private Currency currency;
    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;
    private LocalDateTime updatedAt;
}