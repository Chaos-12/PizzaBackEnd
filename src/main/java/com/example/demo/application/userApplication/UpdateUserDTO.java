package com.example.demo.application.userApplication;

import com.example.demo.domain.userDomain.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String newPassword;
    private Role role;
}