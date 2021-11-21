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

@Service
public class IngredientApplicationImp extends ApplicationBase<Ingredient, UUID> implements IngredientApplication {

    private final IngredientWriteRepository ingredientWriteRepository;
    private final IngredientReadRepository ingredientReadRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public IngredientApplicationImp(final IngredientWriteRepository ingredientWriteRepository,
            final IngredientReadRepository ingredientReadRepository, final ModelMapper modelMapper) {
        super((id) -> ingredientWriteRepository.findById(id));
        this.ingredientWriteRepository = ingredientWriteRepository;
        this.ingredientReadRepository = ingredientReadRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<IngredientDTO> add(CreateOrUpdateIngredientDTO dto) {
        Ingredient ingredient = modelMapper.map(dto, Ingredient.class);
        ingredient.validate("name", ingredient.getName(), name -> this.ingredientWriteRepository.exists(name));
        ingredient.setId(UUID.randomUUID());
        return this.ingredientWriteRepository.save(ingredient, true)
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
            if (dto.getName().equalsIgnoreCase(dbIngredient.getName())) {
                this.modelMapper.map(dto, dbIngredient);
                dbIngredient.validate();
            } else {
                this.modelMapper.map(dto, dbIngredient);
                dbIngredient.validate("name", dbIngredient.getName(),
                        name -> this.ingredientWriteRepository.exists(name));
            }
            return this.ingredientWriteRepository.save(dbIngredient, false)
                    .flatMap(monoIngr -> Mono.just(this.modelMapper.map(monoIngr, IngredientDTO.class)));
        });
    }

    @Override
    public Mono<Void> delete(UUID id) {
        return this.findById(id).flatMap(ingredient -> this.ingredientWriteRepository.delete(ingredient));
    }

    @Override
    public Flux<IngredientProjection> getAll(String name, int page, int size) {
        return this.ingredientReadRepository.getAll(name, page, size);
    }
}