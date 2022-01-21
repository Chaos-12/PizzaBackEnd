package com.example.demo.controller;

import com.example.demo.application.ingredientApplication.CreateOrUpdateIngredientDTO;
import com.example.demo.application.ingredientApplication.IngredientApplication;
import com.example.demo.application.ingredientApplication.IngredientDTO;
import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/ingredients")
public class IngredientController {

    private final IngredientApplication ingredientApplication;

    @Autowired
    public IngredientController(final IngredientApplication ingredientApplication) {
        this.ingredientApplication = ingredientApplication;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<IngredientDTO>> create(@RequestBody final CreateOrUpdateIngredientDTO dto) {
        return this.ingredientApplication.add(dto)
                .map(ingredient -> ResponseEntity.status(HttpStatus.CREATED).body(ingredient));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public Mono<ResponseEntity<IngredientDTO>> get(@PathVariable final String id) {
        return this.ingredientApplication.get(id).map(ingredient -> ResponseEntity.ok(ingredient));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public Mono<ResponseEntity<Void>> update(@PathVariable final String id, @RequestBody CreateOrUpdateIngredientDTO dto) {
        return this.ingredientApplication.update(id, dto)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)));
    }

    @DeleteMapping(path = "/{id}")
    public Mono<ResponseEntity<Void>> delete(@PathVariable final String id) {
        return this.ingredientApplication.delete(id)
                .then(Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(null)));
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<Ingredient> getAll(@RequestParam(required = false) String name) {
        return this.ingredientApplication.getAll(name);
    }
}