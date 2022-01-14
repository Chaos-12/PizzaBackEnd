package com.example.demo.domain.orderDomain;

import java.util.UUID;

import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;
import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.domain.userDomain.UserWriteRepository;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.Setter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Data
@Table("order")
@Setter
public class Order extends EntityBase {
    @NotNull
    private UUID userId;
    @NotNull
    private OrderState state;
    @NotNull
    private UUID[] command;

    public void cancel(){
        if(state.equals(OrderState.submitted)){
            this.state = OrderState.canceled;
        } else {
            throw new BadRequestException("The order is not cancelable");
        }
    }

    public void newCommand(UUID[] newCommand){
        if(state.equals(OrderState.submitted)){
            this.command = newCommand;
            this.validate();
        } else {
            throw new BadRequestException("Unable to modify the command");
        }
    }

    public void validate(){
        super.validate();
        if (command.length < 1){
            throw new BadRequestException("The order must include at least one item");
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
