package com.example.demo.infraestructure.graphQLInfraestructure;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import javax.annotation.PostConstruct;

import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.userDomain.User;
import com.example.demo.infraestructure.ingredientInfraestructure.IngredientRepository;
import com.example.demo.infraestructure.userInfraestructure.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import graphql.GraphQL;
import graphql.schema.DataFetcher;
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
    private final UserRepository userRepository;

    @Autowired
    public GraphQLProvider(final IngredientRepository ingredientRepository,
                    final UserRepository userRepository) {
        this.ingredientRepository = ingredientRepository;
        this.userRepository = userRepository;
    }
    
    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

    @PostConstruct
    public void init() throws IOException {
        File file = resource.getFile();
        TypeDefinitionRegistry typeDefinitionRegistry = new SchemaParser().parse(file);
        RuntimeWiring runtimeWiring = buildRuntimeWiring();
        GraphQLSchema graphQLSchema = new SchemaGenerator().makeExecutableSchema(typeDefinitionRegistry, runtimeWiring);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private RuntimeWiring buildRuntimeWiring() {
        DataFetcher<CompletableFuture<Ingredient>> idIngredientFetcher = 
                new IdDataFetcher<Ingredient, UUID>(ingredientRepository);
        DataFetcher<CompletableFuture<List<Ingredient>>> allIngredientsFetcher = 
                new AllDataFetcher<Ingredient, UUID>(ingredientRepository);
        DataFetcher<CompletableFuture<User>> idUserFetcher = 
                new IdDataFetcher<User, UUID>(userRepository);
        DataFetcher<CompletableFuture<List<User>>> allUsersFetcher = 
                new AllDataFetcher<User, UUID>(userRepository);
        return RuntimeWiring.newRuntimeWiring()
                .type("Query", typeWiring -> typeWiring.dataFetcher("ingredientById", idIngredientFetcher))
                .type("Query", typeWiring -> typeWiring.dataFetcher("allIngredients", allIngredientsFetcher))
                .type("Query", typeWiring -> typeWiring.dataFetcher("userById", idUserFetcher))
                .type("Query", typeWiring -> typeWiring.dataFetcher("allUsers", allUsersFetcher))
                .build();
    }
}