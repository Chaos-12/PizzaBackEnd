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

import org.modelmapper.internal.bytebuddy.asm.Advice.Return;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
@Document(collection = "pizza")
@Setter
public class Pizza extends EntityBase{
    @NotBlank
    private String name;

   // @NotNull
    private UUID image;
    // @DBRef
    private Set<Ingredient> ingredients = new HashSet<Ingredient>();
    
    //@NotNull
    private BigDecimal price = new BigDecimal(0.00);
    

    private final static BigDecimal IngredientProfit =  new BigDecimal(1.20);

    //TODO: comentarios
    
    public Ingredient addIngredient(Ingredient ingredient){
        if(ingredient.getId() != null){
        ingredients.add(ingredient);
        price = price.add(ingredient.getPrice().multiply(IngredientProfit));
        price = price.setScale(2, RoundingMode.HALF_EVEN);}
        return ingredient;
    }

    public void removeIngredient(Ingredient ingredient){
        ingredients.remove(ingredient);
    }

    public Pizza setPrice(){
        BigDecimal price = new BigDecimal(0.00);
        BigDecimal profit = new BigDecimal(1.20);

        for(Ingredient ing : ingredients){
            price = price.add(ing.getPrice());
        }
        price = price.multiply(profit);
        price = price.setScale(2, RoundingMode.HALF_EVEN);

        this.price= price;
            // this.price = new BigDecimal(1.29);
        return this;
    }
    
    // public void setPrice(Flux<Ingredient> ingredients){
    //     BigDecimal price = new BigDecimal(0.00);
    //     BigDecimal profit = new BigDecimal(1.20);
    //     ingredients.map(ing -> price.add(ing.getPrice()))
    //                 .thenMany(a ->{
    //                     price.multiply(profit);
    //                     price.setScale(2, RoundingMode.HALF_EVEN);
    //                     this.price = price;
    //                 });
  
    // }

    @Override
    public String toString() {
        return String.format("Pizza {id: %s, name: %s, price: %s, with ingredients:[%s]}", this.getId(), this.getName(), this.getPrice(), this.getIngredients().toString());
    }
}
