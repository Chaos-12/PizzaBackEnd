package com.example.demo.core;

import java.util.UUID;

import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.core.functionalInterfaces.FindById;

import reactor.core.publisher.Mono;

public abstract class ApplicationBase<T> {
    private FindById<T, UUID> getById;

    protected ApplicationBase(FindById<T, UUID> getById) {
        this.getById = getById;
    }

    protected Mono<T> findById(UUID id) {
        return this.getById
                        .findById(id)
                        .switchIfEmpty(Mono.error(new NotFoundException(
                            String.format("No item found for id %s", id.toString())
                        )));
    }

    protected Mono<T> findById(String id) {
        try {
            return this.findById(ApplicationBase.getUUIDfrom(id));
        } catch (Exception exception) {
            BadRequestException badRequest = new BadRequestException();
            badRequest.addException("Bad Request", "String failed to convert into UUID");
            badRequest.addException("In particular", exception.getMessage());
            return Mono.error(badRequest);
        }
    }

    protected String serializeObject(Object obj, String message) {
        return String.format("%s %s succesfully.", obj.toString(), message);
    }

    public static UUID getUUIDfrom(String id) throws IllegalArgumentException {
        return UUID.fromString(id);
    }
}