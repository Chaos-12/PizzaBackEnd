package com.example.demo.application.ImageApplication.ImageApplication;

import com.example.demo.application.ImageApplication.ImageDTO;
import reactor.core.publisher.Mono;

public interface ImageApplication {
  public Mono<ImageDTO> add(CreateOrUpdateImageDTO dto);
  public Mono<ImageDTO> getImageRedis(String id);
}