package com.example.demo.application.ImageApplication.ImageApplication;

import java.util.UUID;
import com.example.demo.domain.Image;
import reactor.core.publisher.Mono;

public interface ImageApplication {
  public Mono<ImageDTO> add(CreateOrUpdateImageDTO dto);
  public Mono<ImageDTO> getImageRedis(UUID id);

}
