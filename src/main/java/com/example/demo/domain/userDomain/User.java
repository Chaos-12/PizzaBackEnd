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
    public static final int maxRetries = 3;
    @NotNull
    private Role role;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @NotBlank
    private String provider;
    @Email
    @NotNull
    private String email;
    private String password;
    private int tries = maxRetries;

    public String toString(){
        return  String.format("User {id: %s, first_name: %s, last_name: %s, email: %s, role: %s}", 
                    this.getId(), this.getName(), this.getSurname(), this.getEmail(), this.getRole());
    }

    @Override
    public void validate(){
        super.validate();
        if((null == password || password.isBlank() ) && provider.matches("self")){
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.addException("password", "field is required");
            throw badRequestException;
        }
        if(tries <= 0){
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.addException("login failed", "no remaining tries left");
            throw badRequestException;
        }
    }

    public Boolean validate(String possiblePassword){
        this.validate();
        if(!BCrypt.checkpw(possiblePassword, this.getPassword())) {
            tries --;
            BadRequestException badRequestException = new BadRequestException();
            badRequestException.addException("login failed", "wrong password");
            badRequestException.addException("remaining tries", ""+tries);
            throw badRequestException;
        }
        return true;
    }
}