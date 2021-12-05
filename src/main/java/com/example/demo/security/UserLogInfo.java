package com.example.demo.security;

import java.util.HashSet;
import java.util.Set;

import com.example.demo.domain.userDomain.Rol;

public class UserLogInfo {
    private final Rol rol;
    private final Set<String> usedTokens = new HashSet<String>();

    public UserLogInfo(Rol rol){
        this.rol = rol;
    }

    public Rol getRol(){
        return this.rol;
    }

    public Boolean hasUsed(String refreshToken){
        return usedTokens.contains(refreshToken);
    }

    public void addRefresh(String refreshToken){
        this.usedTokens.add(refreshToken);
    }
}