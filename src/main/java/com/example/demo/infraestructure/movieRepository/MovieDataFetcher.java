package com.example.demo.infraestructure.movieRepository;

import com.example.demo.domain.Movie;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import reactor.core.publisher.Flux;

public interface MovieDataFetcher extends DataFetcher<Flux<Movie>> {
    public Flux<Movie> get(DataFetchingEnvironment dataFetchingEnvironment);
}
