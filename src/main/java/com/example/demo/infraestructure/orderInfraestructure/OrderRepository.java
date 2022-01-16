package com.example.demo.infraestructure.orderInfraestructure;

import java.util.UUID;

import com.example.demo.domain.orderDomain.Order;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {
    
}