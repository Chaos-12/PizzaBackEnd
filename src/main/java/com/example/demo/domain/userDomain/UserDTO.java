package com.example.demo.domain.userDomain;

import java.util.UUID;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    private String name;
    private String surname;
    private String email;
    private Role role;
}