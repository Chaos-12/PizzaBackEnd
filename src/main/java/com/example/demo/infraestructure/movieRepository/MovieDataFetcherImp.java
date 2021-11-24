package com.example.demo.infraestructure.movieRepository;

import com.example.demo.domain.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import graphql.schema.DataFetchingEnvironment;
import reactor.core.publisher.Flux;

@Component
public class MovieDataFetcherImp implements MovieDataFetcher {
    private final MovieRepository movieRepository;

    @Autowired
    public MovieDataFetcherImp(final MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Flux<Movie> get(DataFetchingEnvironment dataFetchingEnvironment) {
        return movieRepository.findAll();
    }
}
