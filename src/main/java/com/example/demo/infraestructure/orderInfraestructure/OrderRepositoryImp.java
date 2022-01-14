package com.example.demo.infraestructure.orderInfraestructure;

import java.util.UUID;

import com.example.demo.domain.orderDomain.Order;
import com.example.demo.domain.orderDomain.OrderWriteRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderRepositoryImp implements OrderWriteRepository{
    private final OrderRepository orderRepository;

    @Override
    public Mono<Order> findById(UUID id) {
        return this.orderRepository.findById(id);
    }

    @Override
    public Mono<Order> save(Order order, Boolean isNew) {
        order.setThisNew(isNew);
        return this.orderRepository.save(order);
    }
}