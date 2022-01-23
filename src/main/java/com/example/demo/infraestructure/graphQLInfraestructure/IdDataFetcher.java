package com.example.demo.infraestructure.graphQLInfraestructure;

import java.util.concurrent.CompletableFuture;

import com.example.demo.core.RepositoryBase;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class IdDataFetcher<T, ID> implements DataFetcher<CompletableFuture<T>> {
    private final RepositoryBase<T, ID> repository;
    public IdDataFetcher(final RepositoryBase<T, ID> repository) {
        this.repository = repository;
    }
    @Override
    public CompletableFuture<T> get(DataFetchingEnvironment environment) {
        ID id = environment.getArgument("id");
        return repository.findById(id).toFuture();
    }
}