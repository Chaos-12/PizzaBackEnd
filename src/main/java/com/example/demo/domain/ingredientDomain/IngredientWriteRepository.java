package com.example.demo.domain.ingredientDomain;

import java.util.UUID;

import com.example.demo.core.functionalInterfaces.ExistsByField;
import com.example.demo.core.functionalInterfaces.FindById;

import reactor.core.publisher.Mono;

public interface IngredientWriteRepository extends FindById<Ingredient, UUID>, ExistsByField {
    public Mono<Ingredient> save(Ingredient ingredient, Boolean isNew);
    public Mono<Void> delete(Ingredient ingredient);
}