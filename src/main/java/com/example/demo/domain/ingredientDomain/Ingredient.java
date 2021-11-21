package com.example.demo.domain.ingredientDomain;

import java.math.BigDecimal;

import com.example.demo.core.EntityBase;
import com.example.demo.core.exceptions.BadRequestException;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.Setter;

@Data
@Table("ingredients")
@Setter
public class Ingredient extends EntityBase {
    private String name;
    private BigDecimal price;

    @Override
    public void validate() {
        super.validate();
        if (name == null) {
            throw new BadRequestException("Bad Request: name is null");
        }
        if (name.isBlank()) {
            throw new BadRequestException("Bad Request: name is blank");
        }
        if (price == null) {
            throw new BadRequestException("Bad Request: price is null");
        }
        if (price.doubleValue() < 0) {
            throw new BadRequestException("Bad Request: price is negative");
        }
        if (price.scale() > 2) {
            throw new BadRequestException("Bad Request: price has more than 2 decimal digits");
        }
    }

    @Override
    public String toString() {
        return String.format("Ingredient {id: %s, name: %s, price %s}", this.getId(), this.getName(), this.getPrice());
    }
}