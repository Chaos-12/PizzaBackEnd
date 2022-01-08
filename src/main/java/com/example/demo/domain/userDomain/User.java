package com.example.demo.domain.userDomain;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;
import com.example.demo.core.exceptions.BadRequestException;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

@Data
@Document(collection = "user")
@Setter
@EqualsAndHashCode(callSuper=true)
public class User extends EntityBase {
    public static final int maxRetries = 3;
    @NotNull
    private Role role;
    @NotBlank
    private String name;
    @NotBlank
    private String surname;
    @Email
    @NotNull
    private String email;
    private String password;
    @NotBlank
    private String provider;
    @NotNull
    private int tries;

    public void resetTries() {
        this.tries = User.maxRetries;
    }

    public String toString(){
        return  String.format("User {id: %s, name: %s, surname: %s, email: %s, role: %s}", 
                    this.getId(), this.getName(), this.getSurname(), this.getEmail(), this.getRole());
    }

    @Override
    public void validate(){
        super.validate();
        if((null == password || password.isBlank() ) && provider.matches("self")){
            throw new BadRequestException("password required");
        }
        if(tries <= 0){
            throw new BadRequestException("login failed: no remaining tries left");
        }
    }

    public Boolean validate(String possiblePassword){
        this.validate();
        if(!BCrypt.checkpw(possiblePassword, this.getPassword())) {
            this.tries --;
            return false;
        }
        return true;
    }
}