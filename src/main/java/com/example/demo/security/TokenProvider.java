package com.example.demo.security;

import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.demo.domain.userDomain.User;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

    private static final long JwtTokenValidity = 60*60*1000;

    @Value("#{environment.JwtSecretKey}")
    private String secretKey;

    @Bean
    public TokenProvider create(){
        return new TokenProvider();
    }

    public String generateAccessToken(User user) {
        return Jwts
                .builder()
                .setClaims(new HashMap<String, Object>())
                .setId(NanoIdUtils.randomNanoId())
                .setSubject(user.getId().toString())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtTokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public String generateRefreshToken() {
        return NanoIdUtils.randomNanoId();
    }
}