package com.example.demo.infraestructure.graphQLInfraestructure;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class AllDataFetcher<T, ID> implements DataFetcher<CompletableFuture<List<T>>> {
    private final ReactiveCrudRepository<T, ID> repository;
    public AllDataFetcher(final ReactiveCrudRepository<T, ID> repository) {
        this.repository = repository;
    }
    @Override
    public CompletableFuture<List<T>> get(DataFetchingEnvironment environment) {
        return repository.findAll().collectList().toFuture();
    }
}