package com.example.demo.application.orderApplication;

import java.util.UUID;

import com.example.demo.domain.orderDomain.OrderState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateOrUpdateOrderDTO {
    private OrderState state;
    private UUID[] command;
}
