package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;
import com.example.demo.domain.ingredientDomain.IngredientReadRepository;
import com.example.demo.domain.ingredientDomain.IngredientWriteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class IngredientRepositoryImp implements IngredientWriteRepository, IngredientReadRepository {

    private final IngredientRepository ingredientRepository;

    @Autowired
    public IngredientRepositoryImp(final IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public Mono<Ingredient> findById(UUID id) {
        return this.ingredientRepository.findById(id);
    }

    @Override
    public Mono<Boolean> exists(String name) {
        return this.ingredientRepository.existsByName(name);
    }

    @Override
    public Mono<Ingredient> add(Ingredient ingredient) {
        return this.ingredientRepository.save(ingredient);
    }

    @Override
    public Mono<Ingredient> update(Ingredient ingredient) {
        return this.ingredientRepository.save(ingredient);
    }

    @Override
    public Mono<Void> delete(Ingredient ingredient) {
        return this.ingredientRepository.delete(ingredient);
    }

    @Override
    public Flux<IngredientProjection> getAll(String name, int page, int size) {
        return this.ingredientRepository.findByCriteria(name);
    }
}