package com.example.demo.core;

import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.core.functionalInterfaces.FindById;

import reactor.core.publisher.Mono;

public abstract class ApplicationBase<T, ID> {
    private FindById<T, ID> getById;

    protected ApplicationBase(FindById<T, ID> getById) {
        this.getById = getById;
    }

    protected Mono<T> findById(ID id) {
        return this.getById.findById(id).switchIfEmpty(Mono.error(new NotFoundException()));
    }

    protected String serializeObject(T entity, String message) {
        return String.format("%s %s succesfully.", entity.toString(), message);
    }
}