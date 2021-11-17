package com.example.demo.application.ingredientApplication;

import java.util.UUID;

import com.example.demo.application.ApplicationBase;
import com.example.demo.domain.Ingredient;
import com.example.demo.infraestructure.ingredientInfraestructure.IngredientWriteRepository;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

@Service
public class IngredientApplicationImp extends ApplicationBase<Ingredient, UUID> implements IngredientApplication {

    private final IngredientWriteRepository writeRep;
    private final ModelMapper modelMapper;

    @Autowired
    public IngredientApplicationImp(final IngredientWriteRepository writeRep, final ModelMapper modelMapper) {
        super((id) -> writeRep.findById(id));

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