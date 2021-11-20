package com.example.demo.infraestructure.ImageRepository;

import java.time.Duration;
import com.example.demo.domain.Image;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.core.ReactiveValueOperations;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class ImageRepositoryImp implements ImageRepository {
   /* private final ReactiveRedisTemplate<String,byte[]> reactiveRedisTemplate;
    private final ReactiveValueOperations<String, byte[]> reactiveValueOps;

   
    @Autowired
    public ImageRepositoryImp(final ReactiveRedisTemplate<String,byte[]> reactiveRedisTemplate){
        this.reactiveRedisTemplate = reactiveRedisTemplate;
        this.reactiveValueOps = reactiveRedisTemplate.opsForValue();
    }
    public void add(Image img) {
        reactiveValueOps.set(img.getId().toString(),img.getContent(),Duration.ofDays(1));
        
    }
    */
    private final ReactiveRedisConnectionFactory factory;
    private final ReactiveRedisOperations<String, byte[]> imageOps;
  
    @Autowired
    public ImageRepositoryImp(ReactiveRedisConnectionFactory factory, ReactiveRedisOperations<String, byte[]> imageOps) {
      this.factory = factory;
      this.imageOps = imageOps;
    }
  
    public Mono<Image> add(Image img) {
        imageOps.opsForValue().set(img.getId().toString(), img.getContent(), Duration.ofDays(1));
        
        return Mono.just(img);
    }
}
 