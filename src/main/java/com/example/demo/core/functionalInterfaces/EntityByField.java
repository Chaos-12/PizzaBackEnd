package com.example.demo.core.functionalInterfaces;

import com.example.demo.core.EntityBase;

import reactor.core.publisher.Mono;

public interface EntityByField {
    Mono<EntityBase> getEntity(String field);
}