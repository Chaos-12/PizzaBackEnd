package com.example.demo.security.authFilter;

import com.example.demo.core.exceptions.UnauthorizedException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
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
    private final AuthenticationManager authenticationManager;

    @Override
    public Mono<Void> save(ServerWebExchange serverWebExchange, SecurityContext securityContext) {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public Mono<SecurityContext> load(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                    .switchIfEmpty(Mono.error(new UnauthorizedException("Authorization is required")))
                    .filter(authHeader -> authHeader.startsWith(TOKEN_HEADER))
                    .switchIfEmpty(Mono.error(new UnauthorizedException("Wrong format in authorization token")))
                    .flatMap(authHeader -> {
                        String authToken = authHeader.substring(TOKEN_HEADER.length());
                        Authentication auth = new UsernamePasswordAuthenticationToken(authToken, authToken);
                        return this.authenticationManager.authenticate(auth).map(SecurityContextImpl::new);
                    });
    }
}