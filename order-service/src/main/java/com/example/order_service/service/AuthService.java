package com.example.order_service.service;

import com.example.order_service.dto.*;
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
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public RegisterRequest register(RegisterRequest request){
        User user=userService.register(request);
        return RegisterRequest.builder()
                .userName(user.getUserName())
                .password(user.getPassword())
                .role(user.getRole())
                .build();
    }


    public String login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUserName(),
                            request.getPassword()
                    )
            );
        } catch (Exception ex) {
            throw new UserNotFoundException("Invalid credentials ");
        }

        User user = userRepository.findByUserName(request.getUserName())
                .orElseThrow(() -> new UserNotFoundException("User not found "));


        return jwtUtil.generateTokens(user);
    }

}

