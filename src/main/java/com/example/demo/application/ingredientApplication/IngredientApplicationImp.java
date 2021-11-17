package com.example.demo.application.ingredientApplication;

import java.util.UUID;

import com.example.demo.domain.Ingredient;
import com.example.demo.infraestructure.ingredientInfraestructure.IngredientWriteRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import reactor.core.publisher.Mono;

public class IngredientApplicationImp implements IngredientApplication {

    private final IngredientWriteRepository writeRep;
    private final ModelMapper modelMapper;

    @Autowired
    public IngredientApplicationImp(final IngredientWriteRepository writeRep, final ModelMapper modelMapper) {
        this.writeRep = writeRep;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<IngredientDTO> add(CreateOrUpdateIngredientDTO dto) {
        Ingredient ingredient = modelMapper.map(dto, Ingredient.class);
        ingredient.setId(UUID.randomUUID());
        ingredient.setThisNew(true);

        return this.writeRep.add(ingredient)
                .flatMap(monoIngr -> Mono.just(this.modelMapper.map(monoIngr, IngredientDTO.class)));
    }
}
