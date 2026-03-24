package com.example.order_service.service;

import com.example.order_service.dto.RegisterRequest;
import com.example.order_service.dto.UserResponse;
import com.example.order_service.enums.Role;
import com.example.order_service.exception.UserAlreadyExistException;
import com.example.order_service.model.User;
import com.example.order_service.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public User register(RegisterRequest request) {

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new UserAlreadyExistException("Username already exists");
        }

        Role role = request.getRole() == null ? Role.CUSTOMER : request.getRole();

        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        return userRepository.save(user);
    }
}
