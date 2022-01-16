package com.example.demo.domain.orderDomain;

import java.util.UUID;

import com.example.demo.core.functionalInterfaces.FindById;

import reactor.core.publisher.Mono;

public interface OrderWriteRepository extends FindById<Order, UUID> {
    public Mono<Order> save(Order order, Boolean isNew);
    public Mono<Void> delete(Order order);
}
