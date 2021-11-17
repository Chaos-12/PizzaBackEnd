package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class UserHandlerImp implements UserHandler {

    private final UserRepository userRepository;

    @Autowired
    public UserHandlerImp(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Mono<User> add(final User user) {
        return userRepository.save(user);
    }
}