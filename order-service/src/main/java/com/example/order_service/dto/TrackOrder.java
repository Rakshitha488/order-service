package com.example.order_service.dto;

import com.example.order_service.enums.Status;
import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class TrackOrder {
    private Status status;
    private String orderId;
    private LocalDateTime updatedAt;


}
