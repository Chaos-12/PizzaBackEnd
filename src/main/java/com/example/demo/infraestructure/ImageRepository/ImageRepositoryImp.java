package com.example.demo.infraestructure.imageRepository;

import java.time.Duration;
// import com.example.demo.core.ApplicationBase;
import com.example.demo.core.exceptions.RedisConnectionException;
import com.example.demo.domain.imageDomain.Image;
import com.example.demo.domain.imageDomain.ImageRepository;
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

    public Mono<byte[]> getImageRedis(String id){
        return redisOperations.opsForValue()
                              .get(id);
                              //.onErrorResume(err -> Mono.error(new RedisConnectionException(err.getMessage())));
    }
}