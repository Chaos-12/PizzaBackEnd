package com.example.demo.controller;

import javax.validation.Valid;

import com.example.demo.application.ingredientApplication.CreateOrUpdateIngredientDTO;
import com.example.demo.application.ingredientApplication.IngredientDTO;
import com.example.demo.application.pizzaApplication.CreateOrUpdatePizzaDTO;
import com.example.demo.application.pizzaApplication.PizzaApplication;
import com.example.demo.application.pizzaApplication.PizzaDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/pizzas")
public class PizzaController {
    private final PizzaApplication pizzaApplication;

    @Autowired
    public PizzaController(final PizzaApplication pizzaApplication){
        this.pizzaApplication = pizzaApplication;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<PizzaDTO> create(@Valid @RequestBody final CreateOrUpdatePizzaDTO dto) {
        Mono<PizzaDTO> pizzaDTO = pizzaApplication.add(dto);
        return pizzaDTO;
    }    
}
