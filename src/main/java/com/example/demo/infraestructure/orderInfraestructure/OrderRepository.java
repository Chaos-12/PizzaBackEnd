package com.example.demo.infraestructure.orderInfraestructure;

import java.util.UUID;

import com.example.demo.domain.orderDomain.Order;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends ReactiveMongoRepository<Order, UUID> {
    
    // @Query("SELECT id, user_id, pizza_id, state, address FROM orders ORDER BY state LIMIT :firstIndex, :limit;")
    // public Flux<Order> findAllFrom(int firstIndex, int limit);
}