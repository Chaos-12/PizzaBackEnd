package com.example.demo.security.filter;

import org.springframework.stereotype.Component;

import java.util.UUID;

import com.example.demo.core.exceptions.UnauthorizedException;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.tokens.JwtReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;

import reactor.core.publisher.Mono;

@Component
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtReader jwtUtil;
    private final RedisRepository<UserLogInfo, UUID> logInfoRepository;

    @Autowired
    public AuthenticationManager(final RedisRepository<UserLogInfo, UUID> logInfoRepository,
                    final JwtReader jwtUtil) {
        this.logInfoRepository = logInfoRepository;
        this.jwtUtil = jwtUtil;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        if (jwtUtil.validateToken(authToken)) {
            String id = jwtUtil.getSubjectFromToken(authToken);
            return this.logInfoRepository
                        .getFromID(UUID.fromString(id))
                        .switchIfEmpty(Mono.error(new UnauthorizedException("User must log in")))
                        .map(logInfo -> 
                                new UsernamePasswordAuthenticationToken(
                                    id, null, AuthorityUtils.createAuthorityList(logInfo.getRole().toString())
                                )
                        );
        } else {
            return Mono.error(new UnauthorizedException("Expired token"));
        }
    }
}