package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.core.RepositoryBase;
import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends RepositoryBase<Ingredient, UUID> {

    @Query("SELECT id, name, price FROM ingredient WHERE (name LIKE CONCAT('%', :name, '%')) ORDER BY name;")
    public Flux<IngredientProjection> findByCriteria(String name);
    
    @Query("SELECT id, name, price FROM ingredient ORDER BY name LIMIT :firstIndex, :limit;")
    public Flux<Ingredient> findAllFrom(int firstIndex, int limit);

    @Query("SELECT CASE WHEN COUNT(id)>0 THEN 1 ELSE 0 END FROM ingredient WHERE name = :name;")
    public Mono<Integer> existsByField(String name);
}