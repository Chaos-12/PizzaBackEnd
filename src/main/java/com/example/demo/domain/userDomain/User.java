package com.example.demo.domain.userDomain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.Setter;

@Data
@Table("user")
@Setter
public class User extends EntityBase {
    public static final int max_retries = 3;
    @NotBlank
    private String first_name;
    @NotBlank
    private String last_name;
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String provider;
    @NotNull
    private Rol rol;
    private int remaining_tries = max_retries;

    public String toString(){
        return  String.format("User {id: %s, first_name: %s, last_name: %s, email: %s, rol: %s}", 
                    this.getId(), this.getFirst_name(), this.getLast_name(), this.getEmail(), this.getRol());
    }
}
