package com.example.demo.application.userApplication;

import com.example.demo.domain.userDomain.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String first_name;
    private String last_name;
    private String email;
    private String password;
    private String provider;
    private Role role;
}