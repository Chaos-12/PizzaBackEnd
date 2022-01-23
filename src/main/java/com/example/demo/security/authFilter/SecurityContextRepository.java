package com.example.demo.security.authFilter;

import java.util.UUID;

import com.example.demo.core.exceptions.UnauthorizedException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.authTokens.JwtReader;
import com.example.demo.security.authTokens.OAuthReader;
import com.example.demo.security.authTokens.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private static final String TOKEN_HEADER = "Bearer ";
    private final RedisRepository<UserLogInfo, String> infoRepository;
    private final TokenProvider tokenProvider;
    private final OAuthReader oauthReader;
    private final JwtReader jwtReader;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        if (this.oauthReader.containsOAuthSession(serverWebExchange)) {
            String sessionId = this.oauthReader.getOAuthSession(serverWebExchange);
            return this.infoRepository
                        .set(sessionId, new UserLogInfo(UUID.randomUUID(), Role.ROLE_CUSTOMER), 2)
                        .then();
        }
        return Mono.empty();
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        HttpHeaders headers = serverWebExchange.getRequest().getHeaders();
        if (headers.containsKey(HttpHeaders.AUTHORIZATION)) {
            return Mono.just(headers.getFirst(HttpHeaders.AUTHORIZATION))
                        .filter(authHeader -> authHeader.startsWith(TOKEN_HEADER))
                        .switchIfEmpty(Mono.error(new UnauthorizedException("Wrong format in authorization token")))
                        .flatMap(authHeader -> {
                            String authToken = authHeader.substring(TOKEN_HEADER.length());
                            String userId = jwtReader.getSubjectFromToken(authToken);
                            return this.authenticateFromRedis(userId);
                        });
        }
        if (this.oauthReader.containsOAuthSession(serverWebExchange)){
            String sessionId = this.oauthReader.getOAuthSession(serverWebExchange);
            return this.authenticateFromRedis(sessionId);
        }
        return Mono.error(new UnauthorizedException("Authorization is required"));
    }

    private Mono<SecurityContext> authenticateFromRedis(String id){
        return this.infoRepository
                    .getFromID(id)
                    .switchIfEmpty(Mono.error(new UnauthorizedException("User is not in Redis DB")))
                    .map(logInfo -> tokenProvider.generateAuthenticationToken(logInfo))
                    .map(SecurityContextImpl::new);
    }
}