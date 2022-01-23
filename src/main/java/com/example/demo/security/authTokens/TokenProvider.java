package com.example.demo.security.authTokens;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.security.UserLogInfo;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class TokenProvider {

    private static final long JwtTokenValidity = 60 * 60 * 1000;
    private static final Map<Role, Collection<SimpleGrantedAuthority>> authMap;

    static {
        authMap = new HashMap<Role, Collection<SimpleGrantedAuthority>>();
        for (Role role : Role.values()) {
            authMap.put(role, generateAuthorities(role));
        }
    }

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

    public Authentication generateAuthenticationToken(UserLogInfo logInfo) {
        return generateAuthenticationToken(logInfo.getId().toString(), logInfo.getRole());
    }

    public Authentication generateAuthenticationToken(String id, Role role) {
        return new UsernamePasswordAuthenticationToken(
            id.toString(), null, authMap.get(role)
        );
    }

    private static Collection<SimpleGrantedAuthority> generateAuthorities(Role role) {
        return IntStream.range(role.getLevel(), Role.size)
                        .mapToObj(i -> Role.fromLevel(i).toString())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());
    }
}