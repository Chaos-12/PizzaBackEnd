package com.example.demo.domain.userDomain;

import java.util.UUID;

import com.example.demo.core.functionalInterfaces.ExistsByField;
import com.example.demo.core.functionalInterfaces.FindById;

import reactor.core.publisher.Mono;

public interface UserWriteRepository extends FindById<User, UUID>, ExistsByField {
    public Mono<User> save(User ingredient, Boolean isNew);
    public Mono<User> findUserByEmail(String email);
}