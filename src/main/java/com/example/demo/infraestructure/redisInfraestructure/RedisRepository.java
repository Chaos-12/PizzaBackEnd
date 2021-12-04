package com.example.demo.infraestructure.redisInfraestructure;

import java.time.Duration;

import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.core.exceptions.RedisConnectionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public class RedisRepository<T,ID> {
    private final ReactiveRedisOperations<ID, T> redisOperations;

    @Autowired
    public RedisRepository(final ReactiveRedisOperations<ID, T> redisOperations){
        this.redisOperations = redisOperations;
    }

    public Mono<T> set(ID id, T t, long hours) {
        return redisOperations
                    .opsForValue()
                    .set(id, t, Duration.ofHours(hours))
                    .then(Mono.just(t));
    }

    public Mono<T> getFromString(ID id) {
        return redisOperations
                    .opsForValue()
                    .get(id)
                    .onErrorResume(err -> Mono.error(new RedisConnectionException(err.getMessage())))
                    .switchIfEmpty(Mono.error(new NotFoundException(
                        new StringBuilder("Error: No item found for id ").append(id.toString()).toString())));
    }
}