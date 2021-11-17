package com.example.demo.infraestructure.ingredientInfraestructure;

import com.example.demo.domain.Ingredient;

import reactor.core.publisher.Mono;

public interface IngredientWriteRepository {

    public Mono<Ingredient> add(Ingredient ingredient);
}
