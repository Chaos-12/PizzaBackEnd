package com.example.demo.application.pizzaApplication;

import java.math.BigDecimal;
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
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
        Pizza newPizza = modelMapper.map(dto, Pizza.class);
        for (UUID ingredient : dto.getIngredients()) {
            Mono<IngredientDTO> ingredMono = ingredientApplication.get(ingredient.toString());
            newPizza.addIngredient(modelMapper.map(ingredMono, IngredientDTO.class));
        }  
        newPizza.setPrice(new BigDecimal(0.50));
        newPizza.setId(UUID.randomUUID());

         return newPizza
                .validate("name", newPizza.getName(), name -> this.pizzaWriteRepository.exists(name))
                .then(this.pizzaWriteRepository.save(newPizza, true))
                .map(pizza -> {
                    logger.info(this.serializeObject(pizza, "added"));
                    return this.modelMapper.map(pizza, PizzaDTO.class);
                });
    }
    
}