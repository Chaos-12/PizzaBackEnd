package com.example.demo.core;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

public interface RepositoryBase<T,ID> extends ReactiveCrudRepository<T,ID> {
    public Flux<T> findAllFrom(int firstIndex, int limit);
}
