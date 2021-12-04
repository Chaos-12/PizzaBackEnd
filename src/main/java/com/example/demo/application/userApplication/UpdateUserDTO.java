package com.example.demo.application.userApplication;

import com.example.demo.domain.userDomain.Rol;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserDTO {
    private String first_name;
    private String last_name;
    private String password;
    private String newPassword;
    private String provider;
    private Rol rol;
}