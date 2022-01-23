package com.example.demo.application.userApplication;

import java.util.UUID;

import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.UserDTO;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;

import reactor.core.publisher.Mono;

public interface UserApplication {
    public Mono<AuthResponse> registerNewUser(CreateUserDTO userDto, Role role);
    public Mono<Void> updateUser(String userId, UpdateUserDTO userDto);
    public Mono<AuthResponse> login(AuthRequest userRequest);
    public Mono<AuthResponse> refresh(String refreshToken);
    public Mono<Boolean> logout(UUID userId);
    public Mono<Void> resetTries(String userId);
    public Mono<UserDTO> getProfile(String userId);
}