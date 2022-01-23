package com.example.demo.application.pizzaApplication;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.UUID;

import com.example.demo.application.imageApplication.ImageApplication;
import com.example.demo.application.imageApplication.ImageCloudinaryRepository;
import com.example.demo.application.ingredientApplication.IngredientApplication;
import com.example.demo.application.ingredientApplication.IngredientDTO;
import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.imageDomain.Image;
import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.pizzaDomain.Pizza;
import com.example.demo.domain.pizzaDomain.PizzaReadRepository;
import com.example.demo.domain.pizzaDomain.PizzaWriteRepository;

import org.dataloader.Try;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
@Slf4j
@Service
public class PizzaApplicationImp extends ApplicationBase<Pizza> implements PizzaApplication {
    private final PizzaWriteRepository pizzaWriteRepository;
    private final PizzaReadRepository pizzaReadRepository;
    private final IngredientApplication ingredientApplication;
    private final ImageApplication imageApplication;
    private final ModelMapper modelMapper;
    
    @Autowired
    public PizzaApplicationImp(final PizzaWriteRepository pizzaWriteRepository, 
                            final PizzaReadRepository pizzaReadRepository,
                            final ModelMapper modelMapper,
                            final IngredientApplication ingredientApplication,
                            final ImageApplication imageApplication){
        super((id) -> pizzaWriteRepository.findById(id));
        this.pizzaWriteRepository = pizzaWriteRepository;
        this.pizzaReadRepository = pizzaReadRepository;
        this.ingredientApplication = ingredientApplication;
        this.imageApplication = imageApplication;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<PizzaDTO> add(CreateOrUpdatePizzaDTO dto) {
        Pizza pizza = this.modelMapper.map(dto, Pizza.class);
        pizza.setId(UUID.randomUUID());
        pizza.setThisNew(true);
        pizza.validate("name", pizza.getName(), (name) -> this.pizzaWriteRepository.exists(name));
        //fromIterable

        return Flux.fromIterable(dto.getIngredients())
                    .flatMap(id ->ingredientApplication.get(id.toString()))
                    .doOnNext(dbIngredient -> {
                        Ingredient ingredient = this.modelMapper.map(dbIngredient, Ingredient.class);
                        pizza.addIngredient(ingredient);
                    })
                    .then(pizzaWriteRepository.save(pizza,true))
                    .flatMap(monoPizza -> {
                        log.info(this.serializeObject(pizza, "added"));
                        return Mono.just(this.modelMapper.map(pizza, PizzaDTO.class));
                    });
    }

    @Override
    public Flux<Pizza> getAll(String name) {
        return this.pizzaReadRepository.getAll(name);
    }

    @Override
    public Mono<Pizza> get(String id) {
        return this.findById(id).map(dbPizza -> this.modelMapper.map(dbPizza, Pizza.class));
    }
    
}


