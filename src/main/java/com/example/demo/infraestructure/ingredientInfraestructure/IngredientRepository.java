package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.core.EntityBase;
import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends ReactiveCrudRepository<Ingredient, UUID> {

    @Query("SELECT id, name, price FROM ingredients WHERE (name LIKE CONCAT('%', :name, '%')) ORDER BY name;")
    Flux<IngredientProjection> findByCriteria(String name);

    @Query("SELECT id FROM ingredients WHERE name = :name LIMIT 1;")
    Mono<EntityBase> getEntity(String name);
}