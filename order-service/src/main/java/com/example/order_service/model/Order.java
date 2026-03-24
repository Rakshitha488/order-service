package com.example.order_service.model;


import com.example.order_service.enums.Currency;
import com.example.order_service.enums.Status;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
@Data
@Builder
public class Order {
    @Id
    private String id;
    @Indexed(unique = true)
    private String orderId;
    private String userId;
    private Status status;
    private Currency currency;
    private double totalAmount;
    private LocalDateTime createdAt;
    private boolean deleted ;
    private LocalDateTime UpdatedAt;
    private LocalDateTime deletedAt;


}