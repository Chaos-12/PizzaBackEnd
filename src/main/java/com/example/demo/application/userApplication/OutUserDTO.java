package com.example.demo.application.userApplication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OutUserDTO {
    private static final int defaultExpiration = 3600000;
    private String accessToken;
    private String refreshToken;
    private String type = "Bearer";
    private int expiration = defaultExpiration;
}