package com.example.demo.security.tokens;

import java.util.Date;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

    private static final long JwtTokenValidity = 60*60*1000;

    @Value("#{environment.JwtSecretKey}")
    private String secretKey;

    public String generateAccessToken(String id) {
        return Jwts
                .builder()
                .setId(NanoIdUtils.randomNanoId())
                .setSubject(id)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JwtTokenValidity))
                .signWith(SignatureAlgorithm.HS512, secretKey.getBytes())
                .compact();
    }

    public String generateRefreshToken() {
        return NanoIdUtils.randomNanoId();
    }
}