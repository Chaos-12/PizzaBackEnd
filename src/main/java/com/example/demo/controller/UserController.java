package com.example.demo.controller;

import java.util.UUID;

import com.example.demo.application.userApplication.CreateUserDTO;
import com.example.demo.application.userApplication.UserApplication;
import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;
import com.example.demo.security.tokens.JwtReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserApplication userApplication;
    private final JwtReader jwtReader;

    @Autowired
    public UserController(final UserApplication userApplication, final JwtReader jwtReader) {
        this.userApplication = userApplication;
        this.jwtReader = jwtReader;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/register")
    public Mono<ResponseEntity<AuthResponse>> registerCustomer(@RequestBody final CreateUserDTO dto) {
        return this.userApplication
                    .registerUser(dto, Role.ROLE_CUSTOMER)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/register/employee")
    public Mono<ResponseEntity<AuthResponse>> registerEmployee(@RequestBody final CreateUserDTO dto) {
        return this.userApplication
                    .registerUser(dto, Role.ROLE_EMPLOYEE)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE, path = "/login")
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        return this.userApplication
                    .login(request)
                    .map(response -> ResponseEntity.ok(response));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/refresh/access")
    public Mono<ResponseEntity<AuthResponse>> refreshUser(@RequestParam String refreshToken) {
        return this.userApplication
                    .refresh(refreshToken)
                    .map(response -> ResponseEntity.ok(response));
    }

    @PostMapping(path="/logout")
    public Mono<ResponseEntity<Void>> logout(@RequestHeader("Authorization") String header){
        UUID id = ApplicationBase.getUUIDfrom(jwtReader.getSubjectFromToken(header.substring(7)));
        return this.userApplication
                    .logout(id)
                    .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path="/resetlogintries")
    public Mono<ResponseEntity<Void>> resetLoginTries(@RequestBody final String userId){
        return this.userApplication.resetTries(userId)
                    .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)));
    }
}