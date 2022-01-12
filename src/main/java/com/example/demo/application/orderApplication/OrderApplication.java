package com.example.demo.application.orderApplication;

import reactor.core.publisher.Mono;

public interface OrderApplication {
    public Mono<OrderDTO> add(CreateOrUpdateOrderDTO dto);
}
