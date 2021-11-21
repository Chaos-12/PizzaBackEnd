package com.example.demo.infraestructure.ImageRepository;

import java.time.Duration;

import com.example.demo.domain.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ImageRepositoryImp implements ImageRepository {
    private final ReactiveRedisOperations<String, byte[]> redisOperations;

    @Autowired
    public ImageRepositoryImp(final ReactiveRedisOperations<String, byte[]> redisOperations){
        this.redisOperations = redisOperations;
    }

    public Mono<Image> add(Image image) {
        return redisOperations.opsForValue()
                              .set(image.getId().toString(), image.getContent(),Duration.ofDays(1))
                              .map(img -> image);
    }
 
}
 