package com.example.demo.application.userApplication;

import com.example.demo.domain.userDomain.Role;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;

import reactor.core.publisher.Mono;

public interface UserApplication {
    public Mono<AuthResponse> registerUser(CreateUserDTO dto, Role role);
    public Mono<AuthResponse> login(AuthRequest userRequest);
}