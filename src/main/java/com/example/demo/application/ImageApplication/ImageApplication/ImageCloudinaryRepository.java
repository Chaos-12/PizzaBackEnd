package com.example.demo.application.ImageApplication.ImageApplication;

import reactor.core.publisher.Mono;

public  interface ImageCloudinaryRepository{
    public Mono<ImageDTO> saveImageCloudianary(ImageDTO image);
}
