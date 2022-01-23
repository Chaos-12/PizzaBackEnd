package com.example.demo.core;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import reactor.core.publisher.Flux;

public interface RepositoryBase<T,ID> extends ReactiveMongoRepository<T,ID> {
    //public Flux<T> findAllFrom(int firstIndex, int limit);
}
