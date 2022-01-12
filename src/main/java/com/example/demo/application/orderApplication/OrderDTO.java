package com.example.demo.application.orderApplication;

import com.example.demo.domain.orderDomain.OrderState;
import com.example.demo.domain.userDomain.User;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private User buyer;
    private OrderState state;
    private String[] command;
}
