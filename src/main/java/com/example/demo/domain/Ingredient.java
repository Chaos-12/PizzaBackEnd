package com.example.demo.domain;

import java.math.BigDecimal;

import com.example.demo.core.EntityBase;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Data
@Table("ingredients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Ingredient extends EntityBase {
    @Column
    private String name;
    @Column
    private BigDecimal price;

    @Override
    public String toString() {
        return String.format("Ingredient {id: %s, name: %s, price %s}", this.getId(), this.getName(), this.getPrice());
    }
}