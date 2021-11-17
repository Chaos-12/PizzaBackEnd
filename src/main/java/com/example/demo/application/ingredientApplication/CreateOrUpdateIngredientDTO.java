package com.example.demo.application.ingredientApplication;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateIngredientDTO {
    private String name;
    private BigDecimal price;
}
