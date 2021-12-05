package com.example.demo.security;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.userDomain.Role;

public class UserLogInfo {
    private final Role role;
    private final Set<String> usedTokens = new HashSet<String>();

    public UserLogInfo(Role role){
        this.role = role;
    }

    public Role getRole(){
        return this.role;
    }

    public Boolean hasUsed(String refreshToken){
        return usedTokens.contains(refreshToken);
    }

    public void addRefreshToken(String refreshToken){
        this.usedTokens.add(refreshToken);
    }
}