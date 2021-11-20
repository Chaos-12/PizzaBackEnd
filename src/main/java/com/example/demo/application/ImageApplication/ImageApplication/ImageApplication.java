package com.example.demo.application.ImageApplication.ImageApplication;

import com.example.demo.domain.Image;

import reactor.core.publisher.Mono;

public interface ImageApplication {
    public Mono<Image> save(ImageDTO dto);
}
