package com.example.demo.infraestructure.imageRepository;

import java.time.Duration;
import java.util.UUID;

import com.example.demo.core.exceptions.RedisConnectionException;
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
                                .set(image.getId().toString(), image.getContent(), Duration.ofDays(1))                                                                                        
                                .then(Mono.just(image))
                                .onErrorResume(err -> Mono.error(new RedisConnectionException(err.getMessage())));
    }

    public Mono<Image> getImageRedis(String id){
        return redisOperations.opsForValue()
                              .get(id)
                              .flatMap(imageBytes -> {
                                    Image image = new Image();
                                    image.setContent(imageBytes);
                                    image.setId(UUID.fromString(id));
                                    return Mono.just(image);
                                }).
                                onErrorResume(err -> Mono.error(new RedisConnectionException(err.getMessage())));
    }
}