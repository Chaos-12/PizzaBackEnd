package com.example.demo.controller;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;

@RestController
@RequestMapping("/api/v1/graphQL")
public class GraphQLController {

    @Autowired
    private GraphQL graphQL;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Object getFromQuery(@RequestBody String query) throws InterruptedException, ExecutionException {
        ExecutionInput input = new ExecutionInput(query, null, null, null, new HashMap<String, Object>());
        CompletableFuture<ExecutionResult> completableFuture = graphQL.executeAsync(input);
        return completableFuture.get().getData();
    }
}