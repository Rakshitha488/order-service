package com.example.order_service.dto;


import lombok.Data;


@Data
public class ErrorResponse {
    String errorMessage;

    public ErrorResponse(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
