package com.example.demo.application.pizzaApplication;

import com.example.demo.domain.pizzaDomain.Pizza;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PizzaApplication {
    public Mono<PizzaDTO> add(CreateOrUpdatePizzaDTO pizza);
    public Flux<Pizza> getAll(String name);
    public Mono<Pizza> get(String id);
}