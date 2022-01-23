package com.example.demo.domain.orderDomain;

import java.util.UUID;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;
import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.domain.userDomain.UserWriteRepository;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import reactor.core.publisher.Mono;

@Data
@Document(collection = "order")
@Setter
@EqualsAndHashCode(callSuper=true)
public class Order extends EntityBase {
    @NotNull
    private UUID userId;
    @NotNull
    private UUID pizzaId;
    @NotNull
    private OrderState state;
    @NotNull
    @NotBlank
    private String address;

    public void cancel(){
        if(state.equals(OrderState.submitted)){
            this.state = OrderState.cancelled;
        } else {
            throw new BadRequestException("The order is not cancelable");
        }
    }

    public void newCommand(UUID newCommand){
        if(state.equals(OrderState.submitted)){
            this.pizzaId = newCommand;
            this.validate();
        } else {
            throw new BadRequestException("Unable to modify the command");
        }
    }

    public Mono<Boolean> validateUser(UserWriteRepository userRepository){
        return userRepository.findById(userId)
                    .switchIfEmpty(Mono.error(new NotFoundException("user not found in DB")))
                    .map(user -> true);
    }

    /*public Mono<Boolean> validateCommand(PizzaWriteRepository pizzaRepository){
        return Flux.fromArray(command)
                    .flatMap(pizzaId -> pizzaRepository.getFromId(pizzaId))
                    .switchIfEmpty(Mono.error(new NotFoundException("pizza not found in DB")))
                    .all(pizzaId -> true);
    }*/
}
