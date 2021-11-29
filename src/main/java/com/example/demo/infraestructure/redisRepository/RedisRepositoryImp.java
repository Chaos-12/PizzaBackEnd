package com.example.demo.infraestructure.redisRepository;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Mono;

@Repository
public class RedisRepositoryImp<T> {
    private final ReactiveRedisOperations<String, T> redisOperations;

    @Autowired
    public RedisRepositoryImp(final ReactiveRedisOperations<String, T> redisOperations){
        this.redisOperations = redisOperations;
    }

    public Mono<T> add(String id, T t) {
        return redisOperations.opsForValue()
                              .set(id, t, Duration.ofDays(1))
                              .then(Mono.just(t));
    }

    public Mono<T> getFromString(String string) {
        return redisOperations.opsForValue()
                              .get(string);
    }
}