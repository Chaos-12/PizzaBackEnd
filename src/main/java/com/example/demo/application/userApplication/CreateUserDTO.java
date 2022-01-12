package com.example.demo.application.userApplication;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
}