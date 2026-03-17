package com.example.order_service.dto;

import com.example.order_service.enums.Role;
import lombok.Data;

@Data
public class LoginRequest {
    private String userName;
    private String password;
    private Role role;
}