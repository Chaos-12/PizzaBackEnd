package com.example.demo.application.orderApplication;

import java.util.UUID;

import com.example.demo.domain.orderDomain.OrderState;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDTO {
    private UUID userId;
    private OrderState state;
    private UUID[] command;
}
