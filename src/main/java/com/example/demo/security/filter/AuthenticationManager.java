package com.example.demo.security.filter;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.example.demo.core.exceptions.UnauthorizedException;
import com.example.demo.security.tokens.JwtReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtReader jwtUtil;

    @Autowired
    public AuthenticationManager(final JwtReader jwtUtil){
        this.jwtUtil = jwtUtil;
    }
    
    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String id = jwtUtil.getSubjectFromToken(authToken);
        List<String> rolesMap = Arrays.asList(jwtUtil.getRoleFromToken(authToken));
        return Mono.just(jwtUtil.validateToken(authToken))
                    .filter(valid -> valid)
                    .switchIfEmpty(Mono.error(new UnauthorizedException("Token is not valid")))
                    .thenReturn(new UsernamePasswordAuthenticationToken(id, null,
                            rolesMap.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList())));
    }
}