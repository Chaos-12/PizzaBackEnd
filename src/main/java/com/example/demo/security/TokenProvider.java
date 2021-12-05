package com.example.demo.security;

import java.util.Date;
import java.util.UUID;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

    //@Value("${RedisSecretKey}")
    private String secretKey = "mySecretKey";

    public String generateAccessToken(UUID id){
        /*List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("USER");
         */
        String token = Jwts
                        .builder()
                        .setId("softtekJWT")
                        .setSubject(id.toString())
                        /*.claim("authorities",
                                grantedAuthorities.stream()
                                        .map(GrantedAuthority::getAuthority)
                                        .collect(Collectors.toList()))
                        */
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                        .signWith(SignatureAlgorithm.HS512,secretKey.getBytes())
                        .compact();
        return token;
    }

    public String generateRefreshToken(){
        return NanoIdUtils.randomNanoId();
    }
}