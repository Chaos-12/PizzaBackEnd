package com.example.demo.application.userApplication;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Id;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
public class AutenticationUser {
    @Id
    private UUID id;
    private String access_token;
    private String refresh_token;

    public AutenticationUser(UUID id){
        this.id = id;
        this.setJWTToken(id.toString());
        this.refresh_token = NanoIdUtils.randomNanoId();
    }

    private void setJWTToken(String string){
        String secretKey = "mySecretKey";
        /*List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("USER");
        */
        String token = Jwts
                .builder()
                .setId("softtekJWT")
                .setSubject(string)
                /*.claim("authorities",
                        grantedAuthorities.stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList()))*/
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS512,
                        secretKey.getBytes()).compact();
        this.access_token = token;
    }
}