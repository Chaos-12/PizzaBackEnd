package com.example.demo.domain.userDomain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;
import com.example.demo.core.exceptions.BadRequestException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.Setter;

@Data
@Table("user")
@Setter
public class User extends EntityBase {
    public static final int max_retries = 3;
    @NotNull
    private Role role;
    @NotBlank
    private String first_name;
    @NotBlank
    private String last_name;
    @NotBlank
    private String provider;
    @Email
    @NotNull
    private String email;
    private String password;
    private int remaining_tries = max_retries;

    public String toString(){
        return  String.format("User {id: %s, first_name: %s, last_name: %s, email: %s, rol: %s}", 
                    this.getId(), this.getFirst_name(), this.getLast_name(), this.getEmail(), this.getRole());
    }

    @Override
    public void validate(){
        super.validate();
        if((null == password || password.isBlank() ) && provider.matches("self")){
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.addException("password", "field is required");
            throw badRequestException;
        }
        if(remaining_tries <= 0){
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.addException("login failed", "no remaining tries left");
            throw badRequestException;
        }
    }

    public void validate(String newPassword){
        this.validate();
        if(!BCrypt.checkpw(newPassword, this.getPassword())) {
            remaining_tries --;
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.addException("login failed", "wrong password");
            throw badRequestException;
        }
    }
}