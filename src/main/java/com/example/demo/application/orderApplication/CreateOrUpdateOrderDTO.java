package com.example.demo.application.orderApplication;

import java.util.UUID;

import com.example.demo.domain.orderDomain.OrderState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateOrderDTO {
    private UUID pizzaId;
    private OrderState state;
    private String address;
}
