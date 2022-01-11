package com.example.demo.application.pizzaApplication;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.UUID;
import com.example.demo.application.ImageApplication.ImageApplication.ImageApplication;
import com.example.demo.application.ImageApplication.ImageApplication.ImageCloudinaryRepository;
import com.example.demo.application.ImageApplication.ImageApplication.ImageDTO;
import com.example.demo.application.ingredientApplication.IngredientApplication;
import com.example.demo.application.ingredientApplication.IngredientDTO;
import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.imageDomain.Image;
import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.pizzaDomain.Pizza;
import com.example.demo.domain.pizzaDomain.PizzaWriteRepository;

import org.dataloader.Try;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
@Service
public class PizzaApplicationImp extends ApplicationBase<Pizza> implements PizzaApplication {
    private final ImageCloudinaryRepository imageCloudinaryRepository;
    private final PizzaWriteRepository pizzaWriteRepository;
    private final IngredientApplication ingredientApplication;
    private final ImageApplication imageApplication;
    private final ModelMapper modelMapper;
    private final Logger logger;
    
    @Autowired
    public PizzaApplicationImp(final PizzaWriteRepository pizzaWriteRepository, 
                            final ModelMapper modelMapper,final Logger logger,
                            final IngredientApplication ingredientApplication,
                            final ImageApplication imageApplication,
                            final ImageCloudinaryRepository imageCloudinaryRepository){
        super((id) -> pizzaWriteRepository.findById(id));
        this.pizzaWriteRepository = pizzaWriteRepository;
        this.ingredientApplication = ingredientApplication;
        this.imageApplication = imageApplication;
        this.modelMapper = modelMapper;
        this.logger = logger; 
        this.imageCloudinaryRepository = imageCloudinaryRepository;                   
    }

    @Override
    public Mono<PizzaDTO> add(CreateOrUpdatePizzaDTO dto) {
        Pizza pizza = this.modelMapper.map(dto, Pizza.class);
        pizza.setId(UUID.randomUUID());
        pizza.setPrice(pizza.calculatePrice());
        pizza.setThisNew(true);
        pizza.validate("name", pizza.getName(), (name) -> this.pizzaWriteRepository.exists(name));

        return Flux.fromIterable(dto.getIngredients())
                    .flatMap(id -> ingredientApplication.get(id.toString()))
                    .map(dbIngredient -> {
                        Ingredient ingredient = this.modelMapper.map(dbIngredient, Ingredient.class);
                        return pizza.addIngredient(ingredient);
                    }).then(
                        this.pizzaWriteRepository.save(pizza,true)
                                                 .flatMap(monoPizza -> {
                                                            logger.info(this.serializeObject(pizza, "added"));
                                                            return Mono.just(this.modelMapper.map(pizza, PizzaDTO.class));
                                                    })
                    );   
    }
    
}