package com.example.demo.infraestructure.ImageRepository;

import java.time.Duration;
import com.example.demo.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImp implements ImageRepository {
    private final ReactiveRedisTemplate<String,byte[]> reactiveRedisTemplate;
    private final ReactiveValueOperations<String, byte[]> reactiveValueOps;

   
    @Autowired
    public ImageRepositoryImp(final ReactiveRedisTemplate<String,byte[]> reactiveRedisTemplate){
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.reactiveValueOps = reactiveRedisTemplate.opsForValue();
    }
    public void add(Image img) {
        reactiveValueOps.set(img.getId().toString(),img.getContent(),Duration.ofDays(1));
    }
}
