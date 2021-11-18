package com.example.demo.application.ingredientApplication;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface IngredientApplication {
    public Mono<IngredientDTO> add(CreateOrUpdateIngredientDTO dto);

    public Mono<IngredientDTO> get(UUID id);
}