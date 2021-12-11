package com.example.demo.controller;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionInput;
import graphql.GraphQL;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/graphQL")
public class GraphQLController {

    @Autowired
    private GraphQL graphQL;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Object> getFromQuery(@RequestBody String query) throws InterruptedException, ExecutionException {
        ExecutionInput input = new ExecutionInput(query, null, null, null, new HashMap<String, Object>());
        return Mono.fromFuture(graphQL.executeAsync(input));
    }
}