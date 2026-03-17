package com.example.order_service.model;


import com.example.order_service.enums.Status;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "orders")
@Data
public class Order {
    @Id
    private String id;
    @Indexed(unique = true)
    private String orderId;
    private String userId;
    private Status status;
    private double totalAmount;
    private LocalDateTime createdAt;
    private boolean deleted = false;


}