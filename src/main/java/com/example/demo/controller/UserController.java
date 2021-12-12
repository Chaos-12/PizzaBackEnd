package com.example.demo.controller;

import java.util.UUID;

import com.example.demo.application.userApplication.CreateUserDTO;
import com.example.demo.application.userApplication.UserApplication;
import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.UserDTO;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;
import com.example.demo.security.authTokens.JwtReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserApplication userApplication;
    private final JwtReader jwtReader;

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> registerNewCustomer(@RequestBody final CreateUserDTO dto) {
        return this.userApplication
                    .registerNewUser(dto, Role.ROLE_CUSTOMER)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/employee", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> registerNewEmployee(@RequestBody final CreateUserDTO dto) {
        return this.userApplication
                    .registerNewUser(dto, Role.ROLE_EMPLOYEE)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        return this.userApplication
                    .login(request)
                    .map(response -> ResponseEntity.ok(response));
    }

    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> refreshUser(@RequestParam String refreshToken) {
        return this.userApplication
                    .refresh(refreshToken)
                    .map(response -> ResponseEntity.ok(response));
    }

    @PostMapping(path = "/logout")
    public Mono<ResponseEntity<Void>> logout(@RequestHeader("Authorization") String header){
        UUID userId = ApplicationBase.getUUIDfrom(jwtReader.getSubjectFromToken(header.substring(7)));
        return this.userApplication.logout(userId)
                    .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> resetLoginTries(@RequestParam final String userId){
        return this.userApplication.resetTries(userId)
                    .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)));
    }

    @GetMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<UserDTO>> getProfile(@RequestHeader("Authorization") String header){
        UUID userId = ApplicationBase.getUUIDfrom(jwtReader.getSubjectFromToken(header.substring(7)));
        return this.userApplication.getProfile(userId)
                    .map(userProj -> ResponseEntity.ok(userProj));
    }
}