package com.example.demo.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.Getter;
import lombok.Setter;

@Table("movie")
@Getter
@Setter
public class Movie {
    @Id
    private String id;
    private String title;
    private int series;

    private boolean isThisNew;

    public boolean isNew() {
        return this.isThisNew();
    }
}
