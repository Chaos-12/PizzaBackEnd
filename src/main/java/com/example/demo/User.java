package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity // This tells Hibernate to make a table out of this class
@Table("user")
public @NoArgsConstructor @AllArgsConstructor @Getter @Setter class User {

    @Id
    private Integer id;

    @Column
    private String name;

    @Column
    private String email;

}