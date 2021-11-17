package com.example.demo.infraestructure.ingredientInfraestructure;

import com.example.demo.domain.ingredientDomain.Ingredient;

import reactor.core.publisher.Mono;

public interface IngredientWriteRepository {

    public Mono<Ingredient> add(Ingredient ingredient);
}
