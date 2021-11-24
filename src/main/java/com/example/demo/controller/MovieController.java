package com.example.demo.controller;

import com.example.demo.domain.Movie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionResult;
import graphql.GraphQL;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/v1/movies")
public class MovieController {

    @Autowired
    private GraphQL graphQL;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Movie> getAll(@RequestParam String query) {
        ExecutionResult execute = graphQL.execute(query);
        return execute.getData();
    }

}
