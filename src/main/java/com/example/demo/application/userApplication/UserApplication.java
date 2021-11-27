package com.example.demo.application.userApplication;

import com.example.demo.domain.userDomain.UserProjection;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserApplication {
    public Mono<UserDTO> add(CreateUserDTO dto);
    public Mono<UserDTO> get(String id);
    public Mono<Void> update(String id, UpdateUserDTO dto);
    public Flux<UserProjection> getAll(String email);
}