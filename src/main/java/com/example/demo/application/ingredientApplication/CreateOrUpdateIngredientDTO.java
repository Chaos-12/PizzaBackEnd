package com.example.demo.application.ingredientApplication;

import java.math.BigDecimal;

import com.example.demo.domain.ingredientDomain.IngredientType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateIngredientDTO {
    private String name;
    private BigDecimal price;
    private IngredientType type;
}