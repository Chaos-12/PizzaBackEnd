package com.example.demo.infraestructure.PizzaRepository;

import java.util.UUID;
import com.example.demo.domain.pizzaDomain.Pizza;

//import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface PizzaRepository  extends ReactiveCrudRepository<Pizza, UUID> {

   //TODO: Querys pizza
}
