package com.example.demo.domain.orderDomain;

import java.util.UUID;

import com.example.demo.core.functionalInterfaces.ExistsByField;
import com.example.demo.core.functionalInterfaces.FindById;

import reactor.core.publisher.Mono;

public interface OrderWriteRepository extends FindById<Order, UUID>, ExistsByField{
    public Mono<Order> save(Order ingredient, Boolean isNew);
}
