package com.example.demo.infraestructure.ingredientInfraestructure;

import com.example.demo.domain.Ingredient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public class IngredientRepositoryImp implements IngredientWriteRepository {

    private final IngredientRepository ingredientRep;

    @Autowired
    public IngredientRepositoryImp(final IngredientRepository ingredientRep) {
        this.ingredientRep = ingredientRep;
    }

    public Mono<Ingredient> add(Ingredient ingredient) {
        return this.ingredientRep.save(ingredient);
    }

}
