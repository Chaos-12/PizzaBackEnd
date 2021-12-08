package com.example.demo.security;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.userDomain.Role;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserLogInfo {
    private Set<String> usedTokens = new HashSet<String>();
    private Role role;

    public UserLogInfo(Role role){
        this.role = role;
    }

    public Boolean hasUsed(String refreshToken){
        return usedTokens.contains(refreshToken);
    }

    public void addRefreshToken(String refreshToken){
        this.usedTokens.add(refreshToken);
    }
}