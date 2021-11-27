package com.example.demo.application.imageApplication;

import java.util.UUID;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

//TODO: extends ApplicationBase<Image, UUID>
@Service
public class ImageApplicationImp implements ImageApplication {
  private final ImageRepository imageRepository;
  private final ModelMapper modelMapper;

  @Autowired
  public ImageApplicationImp(final ImageRepository imageRepository, final ModelMapper modelMapper) {
    //super((id) -> imageRepository.findById(id));
    this.imageRepository = imageRepository;
    this.modelMapper = modelMapper;
  }

  public Mono<ImageDTO> add(CreateOrUpdateImageDTO dto){
    Image image = modelMapper.map(dto, Image.class);
    image.setId(UUID.randomUUID());
    image.setThisNew(true);
    return this.imageRepository.add(image).map(monoImage -> this.modelMapper.map(monoImage, ImageDTO.class));
  }

  public Mono<ImageDTO> getImageRedis(String id){
    return this.imageRepository.getImageRedis(id).map(monoImage -> this.modelMapper.map(monoImage, ImageDTO.class));
  }

}
