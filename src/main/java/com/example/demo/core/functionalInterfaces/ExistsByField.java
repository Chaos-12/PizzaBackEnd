package com.example.demo.core.functionalInterfaces;

import com.example.demo.core.EntityBase;

import reactor.core.publisher.Mono;

public interface ExistsByField {
    Mono<EntityBase> exists(String field);
}