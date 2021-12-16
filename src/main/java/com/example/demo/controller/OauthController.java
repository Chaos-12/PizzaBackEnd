package com.example.demo.controller;

import com.example.demo.application.userApplication.CreateUserDTO;
import com.example.demo.application.userApplication.UserApplication;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.security.AuthResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/login/oauth2/code")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OauthController {
    
    private final UserApplication userApplication;
    
    @PostMapping(path = "/github", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> registerNewCustomer() {
        CreateUserDTO dto = new CreateUserDTO();
        return this.userApplication.registerNewUser(dto, Role.ROLE_CUSTOMER)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }
}
