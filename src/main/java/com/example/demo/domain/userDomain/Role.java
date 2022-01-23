package com.example.demo.domain.userDomain;

import java.util.HashMap;
import java.util.Map;

public enum Role {
    ROLE_ADMIN      (0),
    ROLE_EMPLOYEE   (1),
    ROLE_CUSTOMER   (2);

    public static final int size;
    private static final Map<Integer, Role> roleMap;

    static {
        size = values().length;
        roleMap  = new HashMap<Integer, Role>();
        for (Role role: Role.values()) {
            roleMap.put(role.levelCode, role);
        }
    }
    
    private int levelCode;

    private Role(int levelCode){
        this.levelCode = levelCode;
    }

    public int getLevel(){
        return this.levelCode;
    }

    public static Role fromLevel(int level){
        return roleMap.get(level);
    }
}