package com.example.demo.security.authFilter;

import java.util.UUID;

import com.example.demo.core.exceptions.UnauthorizedException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.authTokens.TokenProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.server.context.ServerSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SecurityContextRepository implements ServerSecurityContextRepository {

    private static final String TOKEN_HEADER = "Bearer ";
    private static final String OAUTH_DEFAULT_COOKIE_NAME = "JSESSIONID";
    private final RedisRepository<UserLogInfo, String> infoRepository;
    private final TokenProvider tokenProvider;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        if (securityContext.getAuthentication() instanceof OAuth2AuthenticationToken) {
            String sessionId = serverWebExchange.getRequest().getCookies()
                                                .get(OAUTH_DEFAULT_COOKIE_NAME)
                                                .get(0).getValue();
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
                            return this.authenticateFromRedis(authToken);
                        });
        }
        try{
            String sessionId = serverWebExchange.getRequest().getCookies()
                                            .get(OAUTH_DEFAULT_COOKIE_NAME)
                                            .get(0).getValue();
            return this.authenticateFromRedis(sessionId);
        }catch(Exception ex){
            return Mono.error(new UnauthorizedException("Authorization is required"));
        }
    }

    private Mono<SecurityContext> authenticateFromRedis(String id){
        return this.infoRepository
                    .getFromID(id)
                    .switchIfEmpty(Mono.error(new UnauthorizedException("User is logged out")))
                    .map(logInfo -> tokenProvider.generateAuthenticationToken(logInfo))
                    .map(SecurityContextImpl::new);
    }
}