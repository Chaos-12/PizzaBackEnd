package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.domain.Ingredient;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, UUID> {

    @Query("SELECT CASE WHEN COUNT(ingredient)>0 THEN true ELSE false END FROM ingredient WHERE name = :name")
    Mono<Boolean> existsByName(String name);
}