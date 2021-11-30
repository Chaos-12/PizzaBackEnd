package com.example.demo.controller;

import com.example.demo.application.userApplication.CreateUserDTO;
import com.example.demo.application.userApplication.UserApplication;
import com.example.demo.application.userApplication.UserDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserApplication userApplication;

    @Autowired
    public UserController(final UserApplication userApplication) {
        this.userApplication = userApplication;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/register")
    public Mono<ResponseEntity<UserDTO>> createUser(@RequestBody final CreateUserDTO dto) {
        return this.userApplication.add(dto).map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/refresh")
    public Mono<ResponseEntity<UserDTO>> refreshUser(@RequestBody final String refreshToken) {
        //TODO
        return Mono.empty();
    }
}