package com.example.demo.application.userApplication;

import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;

import reactor.core.publisher.Mono;

public interface UserApplication {
    public Mono<OutUserDTO> registerUser(CreateUserDTO dto);
    public Mono<AuthResponse> login(AuthRequest userRequest);
}