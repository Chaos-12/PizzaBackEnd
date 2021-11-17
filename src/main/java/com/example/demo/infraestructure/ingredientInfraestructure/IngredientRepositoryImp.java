package com.example.demo.infraestructure.ingredientInfraestructure;

import com.example.demo.domain.ingredientDomain.Ingredient;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

public class IngredientRepositoryImp implements IngredientWriteRepository {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientRepositoryImp(final IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    public Mono<Ingredient> add(Ingredient ingredient) {
        return this.ingredientRepository.save(ingredient);
    }

}
