package com.example.demo.controller;

import java.util.UUID;

import com.example.demo.application.userApplication.CreateUserDTO;
import com.example.demo.application.userApplication.UpdateUserDTO;
import com.example.demo.application.userApplication.UserApplication;
import com.example.demo.core.ApplicationBase;
import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.UserDTO;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;
import com.example.demo.security.authTokens.JwtReader;
import com.example.demo.security.authTokens.OAuthReader;

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
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserApplication userApplication;
    private final JwtReader jwtReader;
    private final OAuthReader oAuthReader;

    @PostMapping(path = "/register", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> registerNewCustomer(@RequestBody final CreateUserDTO dto) {
        return this.userApplication.registerNewUser(dto, Role.ROLE_CUSTOMER)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(path = "/employee", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> registerNewEmployee(@RequestBody final CreateUserDTO dto) {
        return this.userApplication.registerNewUser(dto, Role.ROLE_EMPLOYEE)
                    .map(user -> ResponseEntity.status(HttpStatus.CREATED).body(user));
    }

    @PutMapping(path = "/update", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> updateUser(@RequestHeader("Authorization") String authHeader, 
                @RequestBody UpdateUserDTO userDto){
        userDto.setRole(null);
        String userId = jwtReader.getSubjectFromToken(authHeader.substring(7));
        return this.userApplication.updateUser(userId, userDto)
                    .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @PostMapping(path = "/login", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> login(@RequestBody AuthRequest request) {
        return this.userApplication.login(request)
                    .map(response -> ResponseEntity.ok(response));
    }
    
    @PostMapping(path = "/logout")
    public Mono<ResponseEntity<Void>> logout(@RequestHeader("Authorization") String authHeader){
        UUID userId = ApplicationBase.getUUIDfrom(jwtReader.getSubjectFromToken(authHeader.substring(7)));
        return this.userApplication.logout(userId)
                    .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @GetMapping(path = "/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<AuthResponse>> refreshUser(@RequestParam String refreshToken) {
        return this.userApplication.refresh(refreshToken)
                    .map(response -> ResponseEntity.ok(response));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(path = "/reset", consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<Void>> resetLoginTries(@RequestParam final String userId){
        return this.userApplication.resetTries(userId)
                    .thenReturn(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null));
    }

    @GetMapping(path = "/profile", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<UserDTO>> getProfile(@RequestHeader("Authorization") String authHeader){
        String userId = jwtReader.getSubjectFromToken(authHeader.substring(7));
        return this.userApplication.getProfile(userId)
                    .map(userProj -> ResponseEntity.ok(userProj));
    }

    @GetMapping(path = "/profile/oauth", produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<String>> getOauthId(ServerWebExchange serverWebExchange){
        if (this.oAuthReader.containsOAuthSession(serverWebExchange)){
            return Mono.just(ResponseEntity.ok(this.oAuthReader.getOAuthSession(serverWebExchange)));
        }
        return Mono.error(new NotFoundException("User not coming from OAuth service"));
    }
}