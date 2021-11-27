package com.example.demo.application.userApplication;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.domain.userDomain.Rol;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
public class UpdateUserDTO {
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @NotBlank
    private String password;
    @NotBlank
    private String newPassword;
    @NotBlank
    private String provider;
    @NotNull
    private Rol rol;
}