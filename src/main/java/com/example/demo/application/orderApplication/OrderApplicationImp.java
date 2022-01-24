package com.example.demo.application.orderApplication;

import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.orderDomain.Order;
import com.example.demo.domain.orderDomain.OrderReadRepository;
import com.example.demo.domain.orderDomain.OrderWriteRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OrderApplicationImp extends ApplicationBase<Order> implements OrderApplication {

    private final OrderWriteRepository orderWriteRepository;
    private final OrderReadRepository orderReadRepository;
    private final ModelMapper modelMapper;
    
    public OrderApplicationImp(final OrderWriteRepository orderWriteRepository, 
            final OrderReadRepository orderReadRepository, final ModelMapper modelMapper) {
        super((id) -> orderWriteRepository.findById(id));
        this.orderWriteRepository = orderWriteRepository;
        this.orderReadRepository = orderReadRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<OrderDTO> add(UUID userId, CreateOrUpdateOrderDTO dto) {
        Order newOrder = modelMapper.map(dto, Order.class);
        newOrder.setId(UUID.randomUUID());
        newOrder.setUserId(userId);
        newOrder.validate();
        return orderWriteRepository.save(newOrder, true)
                    .map(order -> {
                        log.info(this.serializeObject(order, "added"));
                        return this.modelMapper.map(order, OrderDTO.class);
                    });
    }

    @Override
    public Mono<OrderDTO> get(String id) {
        return this.findById(id).map(dbOrder -> this.modelMapper.map(dbOrder, OrderDTO.class));
    }

    @Override
    public Mono<Void> update(String id, CreateOrUpdateOrderDTO dto) {
        return this.findById(id).flatMap(dbOrder -> {
                        Order newOrder = dbOrder;
                        newOrder.getDeclaredFieldsFrom(dto);
                        return this.orderWriteRepository.save(newOrder, false);
                    }).then();
    }

    @Override
    public Mono<Void> delete(String id) {
        return this.findById(id).flatMap(dbOrder -> this.orderWriteRepository.delete(dbOrder));
    }

    @Override
    public Flux<OrderDTO> getAll() {
        return this.orderReadRepository.getAll()
                        .map(dbOrder -> this.modelMapper.map(dbOrder, OrderDTO.class));
    }

}
