package com.example.demo.infraestructure.ImageRepository;

import java.util.UUID;
import com.example.demo.domain.Image;
import reactor.core.publisher.Mono;

public interface ImageRepository {
    public Mono<Image>  add(Image image);
    public Mono<Image> getImageRedis(UUID uuid);
}
