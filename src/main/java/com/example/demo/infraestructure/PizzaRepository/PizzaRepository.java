package com.example.demo.infraestructure.PizzaRepository;

import java.util.UUID;
import com.example.demo.domain.pizzaDomain.Pizza;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public interface PizzaRepository extends ReactiveCrudRepository<Pizza, UUID> {
   @Query("SELECT CASE WHEN COUNT(id)>0 THEN 1 ELSE 0 END FROM pizza WHERE name = :name;")
   Mono<Integer> existsByField(String name);
}
