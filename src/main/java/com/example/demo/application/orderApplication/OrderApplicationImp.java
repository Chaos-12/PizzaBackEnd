package com.example.demo.application.orderApplication;

import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.orderDomain.Order;
import com.example.demo.domain.orderDomain.OrderWriteRepository;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class OrderApplicationImp extends ApplicationBase<Order> implements OrderApplication {

    private final OrderWriteRepository orderWriteRepository;
    private final ModelMapper modelMapper;
    
    public OrderApplicationImp(OrderWriteRepository orderWriteRepository, ModelMapper modelMapper) {
        super((id) -> orderWriteRepository.findById(id));
        this.orderWriteRepository = orderWriteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Mono<OrderDTO> add(CreateOrUpdateOrderDTO dto) {
        Order newOrder = modelMapper.map(dto, Order.class);
        newOrder.setId(UUID.randomUUID());
        return orderWriteRepository
                    .save(newOrder, true)
                    .map(order -> {
                        log.info(this.serializeObject(order, "added"));
                        return this.modelMapper.map(order, OrderDTO.class);
                    });
    }
}
