package com.example.demo.application.userApplication;

import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String provider;
    private String rol;
    private String type;
    private String token;
}