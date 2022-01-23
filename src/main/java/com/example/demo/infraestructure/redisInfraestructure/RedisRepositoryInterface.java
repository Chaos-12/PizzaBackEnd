package com.example.demo.infraestructure.redisInfraestructure;

import reactor.core.publisher.Mono;

public interface RedisRepositoryInterface<T,ID> {
    public Mono<T> set(ID id, T t, long hours);
    public Mono<T> getFromID(ID id);
    public Mono<Boolean> removeFromID(ID id);
}
