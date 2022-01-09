package com.example.demo.domain.pizzaDomain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.application.ingredientApplication.IngredientDTO;
import com.example.demo.core.EntityBase;
import com.example.demo.domain.ingredientDomain.Ingredient;

import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Setter;
import reactor.core.publisher.Mono;

@Data
@Document(collection = "pizza")
@Setter
public class Pizza extends EntityBase{
    @NotBlank
    private String name;

    @NotNull
    private UUID image;

    private final Set<Ingredient> ingredients = new HashSet<Ingredient>();
    
    @NotNull
    private BigDecimal price;

    //TODO: comentarios
    
    public void addIngredient(Ingredient ingredient){
        ingredients.add(ingredient);
    }

    public void removeIngredient(Ingredient ingredient){
        ingredients.remove(ingredient);
    }

    public BigDecimal calculatePrice(){
        BigDecimal price = new BigDecimal(0.00);
        BigDecimal profit = new BigDecimal(1.20);

        for(Ingredient monoIng : ingredients){
            price = price.add(monoIng.getPrice());
        }
        price = price.multiply(profit);
        price = price.setScale(2, RoundingMode.HALF_EVEN);

       // return price;
       return new BigDecimal(1.20);
    }

    @Override
    public String toString() {
        return String.format("Pizza {id: %s, name: %s, price: %s, with ingredients:[%s]}", this.getId(), this.getName(), this.getPrice(), this.getIngredients().toString());
    }
}
