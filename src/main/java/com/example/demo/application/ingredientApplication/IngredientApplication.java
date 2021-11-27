package com.example.demo.application.ingredientApplication;

import com.example.demo.domain.ingredientDomain.IngredientProjection;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IngredientApplication {
    public Mono<IngredientDTO> add(CreateOrUpdateIngredientDTO dto);
    public Mono<IngredientDTO> get(String id);
    public Mono<Void> update(String id, CreateOrUpdateIngredientDTO dto);
    public Mono<Void> delete(String id);
    public Flux<IngredientProjection> getAll(String name);
}