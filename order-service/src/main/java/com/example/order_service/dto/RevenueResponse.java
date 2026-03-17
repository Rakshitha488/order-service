package com.example.order_service.dto;

import com.example.order_service.enums.Status;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

@Data
public class RevenueResponse {
    @Field("_id")
    private Status status;
    private double totalRevenue;
}
