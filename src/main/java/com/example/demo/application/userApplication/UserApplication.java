package com.example.demo.application.userApplication;

import java.util.UUID;

import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.UserProjection;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;

import reactor.core.publisher.Mono;

public interface UserApplication {
    public Mono<AuthResponse> registerUser(CreateUserDTO dto, Role role);
    public Mono<AuthResponse> login(AuthRequest userRequest);
    public Mono<AuthResponse> refresh(String refreshToken);
    public Mono<Boolean> logout(UUID id);
    public Mono<UserProjection> profile(UUID id);
    public Mono<Void> resetTries(String userId);
}