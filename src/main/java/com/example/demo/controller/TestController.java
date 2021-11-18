package com.example.demo.controller;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.demo.domain.Ingredient;
import com.example.demo.infraestructure.ingredientInfraestructure.IngredientRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/test")
public class TestController {

    private final IngredientRepository ingredientRep;

    @Autowired
    public TestController(final IngredientRepository ingredientRep) {
        this.ingredientRep = ingredientRep;
    }

    @GetMapping(path = "/hello")
    public String testHello() {
        return "Hello World";
    }

    @GetMapping(path = "/mono")
    public Mono<Ingredient> testMono() {
        Ingredient ingr = new Ingredient("testIngr", new BigDecimal(Math.random()));
        ingr.setId(UUID.randomUUID());
        return Mono.just(ingr);
    }

    @GetMapping(path = "/repository")
    public Mono<Ingredient> createUser() {
        Ingredient ingr = new Ingredient("testIngr", new BigDecimal(Math.random()));
        ingr.setId(UUID.randomUUID());
        ingr.setThisNew(true);
        return this.ingredientRep.save(ingr);
    }
}