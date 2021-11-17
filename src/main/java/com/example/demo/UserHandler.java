package com.example.demo;

import reactor.core.publisher.Mono;

public interface UserHandler {
    public Mono<User> createUser(User user);
}