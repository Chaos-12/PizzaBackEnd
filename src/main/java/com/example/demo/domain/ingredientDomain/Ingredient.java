package com.example.demo.domain.ingredientDomain;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.Setter;

@Data
@Table("ingredient")
@Setter
public class Ingredient extends EntityBase {
    @NotBlank
    private String name;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal price;

    @Override
    public String toString() {
        return String.format("Ingredient {id: %s, name: %s, price %s}", this.getId(), this.getName(), this.getPrice());
    }
}
