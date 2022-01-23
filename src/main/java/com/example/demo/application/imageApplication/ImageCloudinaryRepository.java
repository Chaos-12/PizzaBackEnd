package com.example.demo.application.imageApplication;

import reactor.core.publisher.Mono;

public  interface ImageCloudinaryRepository{
    public Mono<ImageDTO> saveImageCloudianary(ImageDTO image);
}
