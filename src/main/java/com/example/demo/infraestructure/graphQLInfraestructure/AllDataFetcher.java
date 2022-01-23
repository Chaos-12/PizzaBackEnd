package com.example.demo.infraestructure.graphQLInfraestructure;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.example.demo.core.RepositoryBase;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

public class AllDataFetcher<T, ID> implements DataFetcher<CompletableFuture<List<T>>> {
    private final RepositoryBase<T, ID> repository;
    public AllDataFetcher(final RepositoryBase<T, ID> repository) {
        this.repository = repository;
    }
    @Override
    public CompletableFuture<List<T>> get(DataFetchingEnvironment environment) {
        Integer start = environment.getArgument("start");
        Integer limit = environment.getArgument("limit");
        if(null == limit){
            return repository.findAll().collectList().toFuture();
        }
        if(null == start){
            start = 0;
        }
        return repository.findAllFrom(start, limit).collectList().toFuture();
    }
}