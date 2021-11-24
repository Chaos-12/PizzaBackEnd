package com.example.demo.infraestructure.PizzaRepository;

import java.util.UUID;
import com.example.demo.core.EntityBase;
import com.example.demo.domain.pizzaDomain.Pizza;
import com.example.demo.domain.pizzaDomain.PizzaWriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class PizzaRepositoryImp implements PizzaWriteRepository  {
    private final PizzaRepository pizzaRepository;

    @Autowired
    public PizzaRepositoryImp(final PizzaRepository pizzaRepository){
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public Mono<Pizza> save(Pizza pizza, Boolean isNew) {
        pizza.setThisNew(isNew);
        
        return pizzaRepository.save(pizza);
    }
    @Override
    public Mono<Pizza> findById(UUID id) {
        return pizzaRepository.findById(id);
    }

    // TODO:
    @Override
    public Mono<EntityBase> getEntity(String field) {
        return null;
    }
}
