package com.example.demo.security.authFilter;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import java.util.UUID;

import com.example.demo.core.exceptions.UnauthorizedException;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.authTokens.JwtReader;
import com.example.demo.security.authTokens.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;

import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final RedisRepository<UserLogInfo, String> logInfoRepository;
    private final TokenProvider tokenProvider;
    private final JwtReader jwtReader;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    public Mono<Authentication> authenticate(String authToken) {
        try {
            UUID userId = UUID.fromString(jwtReader.getSubjectFromToken(authToken));
            return this.logInfoRepository
                        .getFromID(userId.toString())
                        .switchIfEmpty(Mono.error(new UnauthorizedException("User is logged out")))
                        .map(logInfo -> tokenProvider.generateAuthenticationToken(logInfo));
        } catch (Exception ex) {
            return Mono.error(new UnauthorizedException(ex.getMessage()));
        }
    }
}