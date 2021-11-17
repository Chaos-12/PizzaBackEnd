package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class UserHandlerImp implements UserHandler {

    private final UserRepository respository;

    @Autowired
    public UserHandlerImp(final UserRepository respository) {
        this.respository = respository;
    }

    public Mono<User> createUser(final User user) {
        return respository.save(user);
    }
}