package com.example.demo.infraestructure.pizzaRepository;

import java.util.UUID;

import com.example.demo.domain.pizzaDomain.Pizza;
import com.example.demo.domain.pizzaDomain.PizzaReadRepository;
import com.example.demo.domain.pizzaDomain.PizzaWriteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class PizzaRepositoryImp implements PizzaWriteRepository, PizzaReadRepository{
    private final PizzaRepository pizzaRepository;

    @Autowired
    public PizzaRepositoryImp(final PizzaRepository pizzaRepository){
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public Mono<Pizza> findById(UUID id) {
        return this.pizzaRepository.findById(id);
    }

    @Override
    public Mono<Boolean> exists(String name) {
        return Mono.sequenceEqual(this.pizzaRepository.existsByField(name), Mono.just(1));
    }

    @Override
    public Mono<Pizza> save(Pizza pizza, Boolean isNew) {
        pizza.setThisNew(isNew);
        return this.pizzaRepository.save(pizza);
    }

    @Override
    public Flux<Pizza> getAll(String name) {
        return this.pizzaRepository.findAll();
    }

    
}
