package com.example.demo.application.ingredientApplication;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.demo.domain.ingredientDomain.IngredientType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IngredientDTO {
    private UUID id;
    private String name;
    private BigDecimal price;
    private IngredientType type;
}