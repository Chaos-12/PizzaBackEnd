package com.example.demo.application.ingredientApplication;

import java.math.BigDecimal;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.validation.annotation.Validated;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Validated
public class CreateOrUpdateIngredientDTO {
    @NotBlank
    private String name;

    @NotNull
    @Digits(integer = 5, fraction = 2)
    @DecimalMin(value = "0", inclusive = false)
    private BigDecimal price;
}