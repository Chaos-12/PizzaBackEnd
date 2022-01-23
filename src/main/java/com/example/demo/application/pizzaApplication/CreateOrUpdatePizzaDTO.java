package com.example.demo.application.pizzaApplication;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdatePizzaDTO {
    @NotBlank
    private String name;
    @NonNull
    //private UUID image;
    private String image;
    @NotEmpty
    private Set<UUID> ingredients = new HashSet<UUID>();        
}