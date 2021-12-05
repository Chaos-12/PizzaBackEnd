package com.example.demo.security;

import org.springframework.stereotype.Component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtUtil jwtUtil;

    @Autowired
    public AuthenticationManager(final JwtUtil jwtUtil){
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String id = jwtUtil.getSubjectFromToken(authToken);
        return Mono.just(jwtUtil.validateToken(authToken))
                    .then(Mono.just(new UsernamePasswordAuthenticationToken(id, null, null)));
    }
}
