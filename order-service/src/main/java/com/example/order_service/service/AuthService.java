package com.example.order_service.service;

import com.example.order_service.dto.*;
import com.example.order_service.enums.Role;
import com.example.order_service.exception.UserAlreadyExistException;
import com.example.order_service.exception.UserNotFoundException;
import com.example.order_service.repository.UserRepository;
import com.example.order_service.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.order_service.model.User;


@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public String register(RegisterRequest request) {

        Role role = request.getRole() == null ? Role.CUSTOMER : request.getRole();
        User user = User.builder()
                .userName(request.getUserName())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        try {
            userRepository.save(user);
        } catch (Exception ex) {
            throw new UserAlreadyExistException("UserName Already exist....");
        }

        return "User Registerd Successfully " + user.getId();


    }

    public String login(LoginRequest request) {
        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new UserNotFoundException("User not found "));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUserName(),
                        request.getPassword()
                )
        );


        return jwtUtil.generateTokens(user);
    }

}

