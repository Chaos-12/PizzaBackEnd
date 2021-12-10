package com.example.demo.security.tokens;

import java.util.Date;
import java.util.function.Function;

import com.example.demo.core.exceptions.UnauthorizedException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class JwtReader {
    
    @Value("#{environment.JwtSecretKey}")
    private String secretKey;

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey.getBytes()).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public String getSubjectFromToken(String token){
        return this.getAllClaimsFromToken(token).getSubject();
    }

    public String getRoleFromToken(String token){
        return this.getAllClaimsFromToken(token).get("role", String.class);
    }

    public Date getExpirationDateFromToken(String token){
        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public Boolean validateToken(String token) {
        try{
            return !isTokenExpired(token);
        } catch(Exception ex){
            throw new UnauthorizedException(ex.getMessage());
        }
    }
}