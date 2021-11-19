package com.example.demo.infraestructure.ingredientInfraestructure;

import com.example.demo.domain.Ingredient;

import reactor.core.publisher.Flux;

public interface IngredientReadRepository {
    public Flux<Ingredient> getAll(String text, int page, int size);
}