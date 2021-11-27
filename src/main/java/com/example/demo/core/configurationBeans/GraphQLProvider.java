package com.example.demo.core.configurationBeans;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.infraestructure.ingredientInfraestructure.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Component;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;

@Component
public class GraphQLProvider {

    @Value("classpath:schema.graphql")
    private Resource resource;
    private GraphQL graphQL;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public GraphQLProvider(final IngredientRepository ingredientRepository) {
        this.ingredientRepository = ingredientRepository;
    }

    @PostConstruct
    public void init() throws IOException {
        File file = resource.getFile();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(file);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    private RuntimeWiring buildRuntimeWiring() {
        DataFetcher<CompletableFuture<Ingredient>> oneIngrdientFetcher = new byIdDataFetcher<Ingredient, UUID>(
                ingredientRepository);
        DataFetcher<CompletableFuture<List<Ingredient>>> allIngrdientsFetcher = new AllDataFetcher<Ingredient, UUID>(
                ingredientRepository);
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring.dataFetcher("ingredientById", oneIngrdientFetcher))
                .type("Query", typeWiring -> typeWiring.dataFetcher("allIngredients", allIngrdientsFetcher)).build();
    }

    private class byIdDataFetcher<T, ID> implements DataFetcher<CompletableFuture<T>> {
        private final ReactiveCrudRepository<T, ID> repository;
        public byIdDataFetcher(final ReactiveCrudRepository<T, ID> repository) {
            this.repository = repository;
        }
        @Override
        public CompletableFuture<T> get(DataFetchingEnvironment environment) {
            ID id = environment.getArgument("id");
            return repository.findById(id).toFuture();
        }
    }

    private class AllDataFetcher<T, ID> implements DataFetcher<CompletableFuture<List<T>>> {
        private final ReactiveCrudRepository<T, ID> repository;
        public AllDataFetcher(final ReactiveCrudRepository<T, ID> repository) {
            this.repository = repository;
        }
        @Override
        public CompletableFuture<List<T>> get(DataFetchingEnvironment environment) {
            return repository.findAll().collectList().toFuture();
        }
    }
}