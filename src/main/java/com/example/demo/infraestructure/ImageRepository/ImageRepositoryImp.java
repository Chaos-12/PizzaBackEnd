package com.example.demo.infraestructure.ImageRepository;

import java.time.Duration;
import java.util.UUID;

import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.domain.imageDomain.Image;
import com.example.demo.domain.imageDomain.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.relational.core.sql.Not;
import org.springframework.stereotype.Repository;

import io.lettuce.core.Consumer;
import reactor.core.Disposable;
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
                                        .onErrorResume(err -> Mono.error(new NotFoundException()));
    }

    public Mono<Image> getImageRedis(UUID id){
        return redisOperations.opsForValue()
                              .get(id.toString())
                              .flatMap(imageBytes -> {
                                    Image image = new Image();
                                    image.setContent(imageBytes);
                                    image.setId(id);
                                    return Mono.just(image);});
    }
}
 