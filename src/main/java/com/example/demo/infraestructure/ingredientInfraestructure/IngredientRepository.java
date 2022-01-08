package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends ReactiveMongoRepository<Ingredient, UUID> {

    @Query("SELECT id, name, price FROM ingredient WHERE (name LIKE CONCAT('%', :name, '%')) ORDER BY name;")
   //@Query(value="{ 'id' : ?0 }", fields="{ 'firstname' : 1, 'lastname' : 1}") 
   Flux<IngredientProjection> findByCriteria(String name);

    //@Query("SELECT CASE WHEN COUNT(id)>0 THEN 1 ELSE 0 END FROM ingredient WHERE name = :name;")
    @Query("{ ?0: { $exists: true } }")
    Mono<Integer> existsByField(String name);
}