package com.example.demo.infraestructure.ingredientInfraestructure;

import java.util.UUID;

import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface IngredientRepository extends ReactiveMongoRepository<Ingredient, UUID> {

    //@Query("SELECT id, name, price FROM ingredient WHERE (name LIKE CONCAT('%', :name, '%')) ORDER BY name;")
    //@Query(value="{ 'id' : ?0 }", fields="{ 'firstname' : 1, 'lastname' : 1}")
    @Query("{ name : /$0/}")
    Flux<IngredientProjection> findByCriteria(String name);

    //@Query("{ name: { $exists: true, $nin: [ $0 ] } }")
    @Query("{$0: {$exists: true } }")
    Mono<Ingredient> existsByField(String name);
}