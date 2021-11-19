package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.domain.Ingredient;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, UUID> {

    @Query("SELECT * FROM ingredients WHERE name LIKE CONCAT('%',:text,'%')")
    Flux<Ingredient> findByName(String text, Pageable pageable);

    @Query("SELECT CASE WHEN COUNT(ingredient)>0 THEN true ELSE false END FROM ingredient WHERE name = :name")
    Mono<Boolean> existsByName(String name);
}