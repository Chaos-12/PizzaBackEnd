package com.example.demo.application.orderApplication;

import java.util.ArrayList;

import com.example.demo.domain.orderDomain.OrderState;
import com.example.demo.domain.userDomain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateOrderDTO {
    private User buyer;
    private OrderState state;
    private ArrayList<String> command;
}
