package com.example.demo.infraestructure.pizzaRepository;

import java.util.UUID;
import com.example.demo.domain.ingredientDomain.IngredientProjection;
import com.example.demo.domain.pizzaDomain.Pizza;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface PizzaRepository extends ReactiveMongoRepository<Pizza, UUID> {
    @Query("{ ?0: { $exists: true } }")
    Mono<Integer> existsByField(String name);
}