package com.example.demo;

import java.util.UUID;

import com.example.demo.domain.EntityBase;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table("user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User extends EntityBase {

    private String name;
    private String email;

    @Override
    public boolean isNew() {
        return this.isThisNew();
    }

}