package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.core.functionalInterfaces.ExistsByField;
import com.example.demo.core.functionalInterfaces.FindById;
import com.example.demo.domain.Ingredient;

import reactor.core.publisher.Mono;

public interface IngredientWriteRepository extends FindById<Ingredient, UUID>, ExistsByField {
    public Mono<Ingredient> add(Ingredient ingredient);
}