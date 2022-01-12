package com.example.demo.domain.orderDomain;

import java.util.ArrayList;

import javax.validation.constraints.NotNull;

import com.example.demo.core.EntityBase;
import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.domain.userDomain.User;

import org.springframework.data.relational.core.mapping.Table;

import lombok.Data;
import lombok.Setter;

@Data
@Table("order")
@Setter
public class Order extends EntityBase {
    @NotNull
    private User buyer;
    @NotNull
    private OrderState state;
    @NotNull
    private String[] command;

    public void cancel(){
        if(state.equals(OrderState.STATE_ORDERED)){
            this.state = OrderState.STATE_CANCELED;
        } else {
            throw new BadRequestException("The order is not cancelable");
        }
    }

    public void newCommand(String[] newCommand){
        if(state.equals(OrderState.STATE_ORDERED)){
            this.command = newCommand;
            this.validate();
        } else {
            throw new BadRequestException("Unable to modify the command");
        }
    }

    public void validate(){
        super.validate();
        if (command.length < 1){
            throw new BadRequestException("The order must include at least one pizza");
        }
    }
}
