package com.example.demo.domain.pizzaDomain;

import java.util.UUID;
import com.example.demo.core.functionalInterfaces.EntityByField;
import com.example.demo.core.functionalInterfaces.FindById;
import reactor.core.publisher.Mono;

public interface PizzaWriteRepository extends FindById<Pizza, UUID>, EntityByField  {
    public Mono<Pizza> save(Pizza pizza, Boolean isNew);
}
