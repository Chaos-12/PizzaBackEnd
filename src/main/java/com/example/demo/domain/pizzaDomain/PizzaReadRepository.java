package com.example.demo.domain.pizzaDomain;

import reactor.core.publisher.Flux;

public interface PizzaReadRepository {
    public Flux<Pizza> getAll(String name);
}