package com.example.demo.infraestructure.orderInfraestructure;

import java.util.UUID;

import com.example.demo.core.RepositoryBase;
import com.example.demo.domain.orderDomain.Order;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;

@Repository
public interface OrderRepository extends RepositoryBase<Order, UUID> {
    
    @Query("SELECT id, user_id, pizza_id, state, address FROM orders ORDER BY state LIMIT :firstIndex, :limit;")
    public Flux<Order> findAllFrom(int firstIndex, int limit);
}