package com.example.demo.application.ingredientApplication;

import java.util.UUID;
import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.ingredientDomain.Ingredient;
import com.example.demo.domain.ingredientDomain.IngredientProjection;
import com.example.demo.domain.ingredientDomain.IngredientReadRepository;
import com.example.demo.domain.ingredientDomain.IngredientWriteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;

@Service
public class IngredientApplicationImp extends ApplicationBase<Ingredient> implements IngredientApplication {

    private final IngredientWriteRepository ingredientWriteRepository;
    private final IngredientReadRepository ingredientReadRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    @Autowired
    public IngredientApplicationImp(final IngredientWriteRepository ingredientWriteRepository,
            final IngredientReadRepository ingredientReadRepository, final ModelMapper modelMapper,
            final Logger logger) {
        super((id) -> ingredientWriteRepository.findById(id));
        this.ingredientWriteRepository = ingredientWriteRepository;
        this.ingredientReadRepository = ingredientReadRepository;
        this.modelMapper = modelMapper;
        this.logger = logger;
    }

    @Override
    public Mono<IngredientDTO> add(CreateOrUpdateIngredientDTO dto) {
        Ingredient newIngredient = modelMapper.map(dto, Ingredient.class);
        newIngredient.setId(UUID.randomUUID());
        return newIngredient
                .validate("name", newIngredient.getName(), name -> this.ingredientWriteRepository.exists(name))
                .then(this.ingredientWriteRepository.save(newIngredient, true))
                .map(ingredient -> {
                    logger.info(this.serializeObject(ingredient, "added"));
                    return this.modelMapper.map(ingredient, IngredientDTO.class);
                });
    }

    @Override
    public Mono<IngredientDTO> get(String id) {
        return this.findById(id).map(dbIngredient -> this.modelMapper.map(dbIngredient, IngredientDTO.class));
    }

    @Override
    public Mono<Void> update(String id, CreateOrUpdateIngredientDTO dto) {
        return this.findById(id).flatMap(dbIngredient -> {
            if (dto.getName().equalsIgnoreCase(dbIngredient.getName())) {
                this.modelMapper.map(dto, dbIngredient);
                dbIngredient.validate();
                return this.ingredientWriteRepository.save(dbIngredient, false)
                        .flatMap(ingredient -> {
                            logger.info(this.serializeObject(ingredient, "updated"));
                            return Mono.empty();
                        });
            } else {
                this.modelMapper.map(dto, dbIngredient);
                return dbIngredient
                        .validate("name", dbIngredient.getName(), name -> this.ingredientWriteRepository.exists(name))
                        .then(this.ingredientWriteRepository.save(dbIngredient, false))
                        .flatMap(ingredient -> {
                            logger.info(this.serializeObject(ingredient, "updated"));
                            return Mono.empty();
                        });
            }
        });
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.findById(id).flatMap(dbIngredient -> this.ingredientWriteRepository.delete(dbIngredient));
    }

    @Override
    public Flux<IngredientProjection> getAll(String name) {
        return this.ingredientReadRepository.getAll(name);
    }
}