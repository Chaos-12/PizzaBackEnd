package com.example.demo.infraestructure.orderInfraestructure;

import java.util.UUID;

import com.example.demo.core.RepositoryBase;
import com.example.demo.domain.orderDomain.Order;

public interface OrderRepository extends RepositoryBase<Order, UUID> {
    
}