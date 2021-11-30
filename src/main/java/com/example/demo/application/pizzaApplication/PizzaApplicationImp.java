package com.example.demo.application.pizzaApplication;

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
    public Mono<PizzaDTO> add(CreateOrUpdatePizzaDTO pizzaDTO) {
        //validar antes de nada
        Pizza newPizza = modelMapper.map(pizzaDTO, Pizza.class);
        newPizza.setId(UUID.randomUUID());
        for (UUID ingredientId : pizzaDTO.getIngredients()) {
            IngredientDTO newIngredient = ingredientApplication.get(ingredientId.toString()).block();
            newPizza.addIngredient(modelMapper.map(newIngredient, Ingredient.class));
        }
        newPizza.setPrice(newPizza.calculatePrice());
        //byte[] bytesImg = modelMapper.map(imageApplication.getImageRedis(pizzaDTO.getImage().toString()),byte[].class);
        byte[] bytesImg = imageApplication.getImageRedis(pizzaDTO.getImage().toString()).block();
        
        ImageDTO image = new ImageDTO();
        image.setId(pizzaDTO.getImage());
        image.setContent(bytesImg);
        Mono<ImageDTO> cloud = imageCloudinaryRepository.saveImageCloudianary(image);
        newPizza.setImage(image.getId());

        return newPizza.validate("name", newPizza.getName(), name -> pizzaWriteRepository.exists(name))
                .then(pizzaWriteRepository.save(newPizza, true))
                .flatMap(pizza -> {
                        logger.info(this.serializeObject(pizza,"added"));
                        return Mono.just(modelMapper.map(pizza, PizzaDTO.class));
                });
    }
    
}
