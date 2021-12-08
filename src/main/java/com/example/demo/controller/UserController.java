package com.example.demo.controller;

import com.example.demo.application.userApplication.CreateUserDTO;
import com.example.demo.application.userApplication.OutUserDTO;
import com.example.demo.application.userApplication.UserApplication;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;

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
    public Mono<ResponseEntity<AuthResponse>> registerCustomer(@RequestBody final CreateUserDTO dto) {
        return this.userApplication
                    .registerUser(dto, Role.ROLE_CUSTOMER)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        return this.userApplication
                    .login(request)
                    .map(authResponse -> ResponseEntity.ok(authResponse));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/refresh")
    public Mono<ResponseEntity<OutUserDTO>> refreshUser(@RequestBody final String refreshToken) {
        //TODO
        return Mono.empty();
    }
}