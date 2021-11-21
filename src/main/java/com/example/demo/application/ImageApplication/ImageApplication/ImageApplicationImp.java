package com.example.demo.application.ImageApplication.ImageApplication;

import java.util.UUID;

import com.example.demo.domain.Image;
import com.example.demo.infraestructure.ImageRepository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class ImageApplicationImp implements ImageApplication {
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ImageApplicationImp(final ImageRepository imageRepository, final ModelMapper modelMapper) {
    this.imageRepository = imageRepository;
    this.modelMapper = modelMapper;
  }

  public Mono<ImageDTO> add(CreateOrUpdateImageDTO dto){
    Image image = modelMapper.map(dto, Image.class);
    image.setId(UUID.randomUUID());
    image.setThisNew(true);
    return this.imageRepository.add(image)
                          .flatMap(monoImage -> Mono.just(this.modelMapper.map(monoImage, ImageDTO.class)));
  }

}
