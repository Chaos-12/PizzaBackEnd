package com.example.demo.infraestructure.movieRepository;

import com.example.demo.domain.Movie;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface MovieRepository extends ReactiveCrudRepository<Movie, String> {

}
