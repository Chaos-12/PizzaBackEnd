package com.example.demo.security.authTokens;

import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class OAuthReader {

    // public User getUser(OAuth2User principal, String provider) {
    // }

    public String getUserInfo(OAuth2User principal){
        StringBuilder output = new StringBuilder("User OAuth2 profile:\n");
        output.append(principal.getName());
        return output.toString();
    }
}
