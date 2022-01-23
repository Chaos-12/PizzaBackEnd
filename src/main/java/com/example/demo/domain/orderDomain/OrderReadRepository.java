package com.example.demo.domain.orderDomain;

import reactor.core.publisher.Flux;

public interface OrderReadRepository {
    public Flux<Order> getAll(int firstIndex, int limit);
}