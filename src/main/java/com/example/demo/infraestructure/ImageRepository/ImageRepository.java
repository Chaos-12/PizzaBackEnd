package com.example.demo.infraestructure.ImageRepository;

import com.example.demo.domain.Image;

import reactor.core.publisher.Mono;

public interface ImageRepository {
    public  Mono<Image>  add(Image image);
}
