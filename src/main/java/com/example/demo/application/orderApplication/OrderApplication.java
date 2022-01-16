package com.example.demo.application.orderApplication;

import java.util.UUID;

import reactor.core.publisher.Mono;

public interface OrderApplication {
    public Mono<OrderDTO> add(UUID userId, CreateOrUpdateOrderDTO dto);

    public Mono<OrderDTO> get(String id);

    public Mono<Void> update(String id, CreateOrUpdateOrderDTO dto);

    public Mono<Void> delete(String id);
}
