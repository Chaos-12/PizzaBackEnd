package com.example.demo.domain.ingredientDomain;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

// enum INGREDIENT_TYPE
// {
//     INGREDIENT, BASE;
// }

@Data
@Document(collection = "ingredient")
@Setter
@EqualsAndHashCode(callSuper=true)
public class Ingredient extends EntityBase {
    @NotBlank
    private String name;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal price;
    // private INGREDIENT_TYPE type;

    @Override
    public String toString() {
        return String.format("Ingredient {id: %s, name: %s, price %s}", this.getId(), this.getName(), this.getPrice());
    }
}
