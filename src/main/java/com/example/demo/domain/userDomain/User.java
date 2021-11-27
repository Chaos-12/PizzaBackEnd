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
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @Email
    private String email;
    @NotBlank
    private String password;
    @NotBlank
    private String provider;
    @NotNull
    private Rol rol;

    public String toString(){
        return  String.format("User {id: %s, firstName: %s, lastName: %s, email: %s, rol: %s}", 
                    this.getId(), this.getFirstName(), this.getLastName(), this.getEmail(), this.getRol());
    }
}
