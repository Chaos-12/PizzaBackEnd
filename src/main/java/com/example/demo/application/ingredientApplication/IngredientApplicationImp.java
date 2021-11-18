package com.example.demo.application.ingredientApplication;

import java.util.UUID;

import com.example.demo.application.ApplicationBase;
import com.example.demo.domain.Ingredient;
import com.example.demo.infraestructure.ingredientInfraestructure.IngredientReadRepository;
import com.example.demo.infraestructure.ingredientInfraestructure.IngredientWriteRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class IngredientApplicationImp extends ApplicationBase<Ingredient, UUID> implements IngredientApplication {

    private final IngredientWriteRepository ingredientWriteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public IngredientApplicationImp(final IngredientWriteRepository ingredientWriteRepository,
            final ModelMapper modelMapper) {
        super((id) -> ingredientWriteRepository.findById(id));
        this.ingredientWriteRepository = ingredientWriteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<IngredientDTO> add(CreateOrUpdateIngredientDTO dto) {
        Ingredient ingredient = modelMapper.map(dto, Ingredient.class);
        ingredient.setId(UUID.randomUUID());
        ingredient.setThisNew(true);
        return this.ingredientWriteRepository.add(ingredient)
                .flatMap(monoIngr -> Mono.just(this.modelMapper.map(monoIngr, IngredientDTO.class)));
    }

    @Override
    public Mono<IngredientDTO> get(UUID id) {
        return ingredientWriteRepository.findById(id)
                .flatMap(monoIngr -> Mono.just(this.modelMapper.map(monoIngr, IngredientDTO.class)));
    }

    @Override
    public Mono<IngredientDTO> update(UUID id, CreateOrUpdateIngredientDTO dto) {
        return this.findById(id).flatMap(dbIngredient -> {
            if (dbIngredient.getName().equals(dto.getName())) {
                this.modelMapper.map(dto, dbIngredient);
                // dbIngredient.validate();
                return this.ingredientWriteRepository.update(dbIngredient)
                        .flatMap(ingredient -> Mono.just(this.modelMapper.map(ingredient, IngredientDTO.class)));
            } else {
                this.modelMapper.map(dto, dbIngredient);
                // dbIngredient.validate("name", dbIngredient.getName(), (name) ->
                // this.ingredientWriteRepository.exists(name));
                return this.ingredientWriteRepository.update(dbIngredient).flatMap(ingredient -> {
                    // logger.info(this.serializeObject(dbIngredient, "updated"));
                    return Mono.just(this.modelMapper.map(ingredient, IngredientDTO.class));
                });
            }
        });
    }

}