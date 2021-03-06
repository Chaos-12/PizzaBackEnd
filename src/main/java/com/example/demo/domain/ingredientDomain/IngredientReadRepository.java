package com.example.demo.domain.ingredientDomain;

import reactor.core.publisher.Flux;

public interface IngredientReadRepository {
    public Flux<Ingredient> getAll(String name);
}