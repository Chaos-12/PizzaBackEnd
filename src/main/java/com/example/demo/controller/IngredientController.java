package com.example.demo.controller;

import java.util.UUID;

import com.example.demo.application.ingredientApplication.CreateOrUpdateIngredientDTO;
import com.example.demo.application.ingredientApplication.IngredientApplication;
import com.example.demo.application.ingredientApplication.IngredientDTO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<IngredientDTO> create(@RequestBody CreateOrUpdateIngredientDTO dto) {
        Mono<IngredientDTO> ingredientDTO = this.ingredientApplication.add(dto);
        return ingredientDTO;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE, path = "/{id}")
    public Mono<ResponseEntity<IngredientDTO>> existsName(@PathVariable UUID id) {
        Mono<IngredientDTO> ingredientDTO = this.ingredientApplication.get(id);
        return ingredientDTO.map(ingredient -> ResponseEntity.ok(ingredient))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}