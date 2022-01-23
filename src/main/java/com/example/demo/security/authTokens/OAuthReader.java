package com.example.demo.security.authTokens;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

@Component
public class OAuthReader {
    
    private static final String OAUTH_DEFAULT_COOKIE_NAME = "SESSION";

    public Boolean containsOAuthSession(ServerWebExchange serverWebExchange){
        return serverWebExchange.getRequest().getCookies()
                                .containsKey(OAUTH_DEFAULT_COOKIE_NAME);
    }

    public String getOAuthSession(ServerWebExchange serverWebExchange){
        return serverWebExchange.getRequest().getCookies()
                                .get(OAUTH_DEFAULT_COOKIE_NAME)
                                .get(0).getValue();
    }
}
