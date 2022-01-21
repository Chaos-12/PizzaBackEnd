package com.example.demo.controller;

import com.example.demo.application.pizzaApplication.CreateOrUpdatePizzaDTO;
import com.example.demo.application.pizzaApplication.PizzaApplication;
import com.example.demo.application.pizzaApplication.PizzaDTO;
import com.example.demo.domain.pizzaDomain.Pizza;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Flux;
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
    public Mono<PizzaDTO> create(@RequestBody final CreateOrUpdatePizzaDTO dto) {
        return pizzaApplication.add(dto);
    }    

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Pizza> getAll(@RequestParam(required = false) String name) {
        return pizzaApplication.getAll(name);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public Mono<ResponseEntity<Pizza>> get(@PathVariable final String id) {
        return this.pizzaApplication.get(id).map(ingredient -> ResponseEntity.ok(ingredient));
    }
}
