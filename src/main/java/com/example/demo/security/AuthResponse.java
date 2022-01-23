package com.example.demo.security;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthResponse {
    private static final int defaultExpiration = 60*60*1000;
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private int expiration = defaultExpiration;
}