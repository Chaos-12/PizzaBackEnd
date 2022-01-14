package com.example.demo.controller;

import java.util.UUID;

import com.example.demo.application.orderApplication.CreateOrUpdateOrderDTO;
import com.example.demo.application.orderApplication.OrderApplication;
import com.example.demo.application.orderApplication.OrderDTO;
import com.example.demo.core.ApplicationBase;
import com.example.demo.security.authTokens.JwtReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderController {
    
    private final OrderApplication orderApplication;
    private final JwtReader jwtReader;

    @PreAuthorize("hasRole('EMPLOYEE')")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Mono<ResponseEntity<OrderDTO>> newOrder(@RequestHeader("Authorization") String authHeader, @RequestBody final CreateOrUpdateOrderDTO dto) {
        UUID userId = ApplicationBase.getUUIDfrom(jwtReader.getUserId(authHeader));
        return this.orderApplication.add(userId, dto)
                .map(order -> ResponseEntity.status(HttpStatus.CREATED).body(order));
    }
}
