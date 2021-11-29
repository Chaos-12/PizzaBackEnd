package com.example.demo.application.userApplication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private static final int defaultExpiration = 3600000;
    private String access_token;
    private String refresh_token;
    private String type = "Bearer";
    private int expiration = defaultExpiration;
}