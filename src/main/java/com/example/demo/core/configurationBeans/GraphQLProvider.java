package com.example.demo.core.configurationBeans;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;

import com.example.demo.infraestructure.movieRepository.MovieDataFetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class GraphQLProvider {

    @Autowired
    private MovieDataFetcher movieDataFetcher;

    @Value("classpath:schema.graphql")
    private Resource resource;

    private GraphQL graphQL;

    /*
     * @Autowired public GraphQLProvider(final IngredientDataFetcher
     * ingredientDataFetcher) { this.ingredientDataFetcher = ingredientDataFetcher;
     * }
     */

    @PostConstruct
    public void init() throws IOException {
        File file = resource.getFile();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(file);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring.dataFetcher("allMovies", movieDataFetcher)).build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }
}
