package com.example.demo.application.pizzaApplication;

import reactor.core.publisher.Mono;

public interface PizzaApplication {
    public Mono<PizzaDTO> add(CreateOrUpdatePizzaDTO pizza);
}