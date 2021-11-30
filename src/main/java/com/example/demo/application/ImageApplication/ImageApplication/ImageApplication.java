package com.example.demo.application.ImageApplication.ImageApplication;

import reactor.core.publisher.Mono;

public interface ImageApplication {
  public Mono<ImageDTO> add(CreateOrUpdateImageDTO dto);
  public Mono<byte[]> getImageRedis(String id);
}