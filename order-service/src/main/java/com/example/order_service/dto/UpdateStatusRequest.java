package com.example.order_service.dto;

import com.example.order_service.enums.Status;
import lombok.Data;

@Data
public class UpdateStatusRequest {
    private Status status;
}
