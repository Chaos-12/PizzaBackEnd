package com.example.demo.application.ImageApplication.ImageApplication;

import java.util.UUID;
import com.example.demo.domain.Image;
import com.example.demo.infraestructure.ImageRepository.ImageRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ImageApplicationImp implements ImageApplication {
    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ImageApplicationImp(final ImageRepository imageRepository, final ModelMapper modelMapper){
      //super((id) -> imageRepository.findById(id));
      this.imageRepository = imageRepository;
      this.modelMapper = modelMapper;
    }
    public Mono<Image> save(ImageDTO dto){
      Image image = modelMapper.map(dto, Image.class);
      image.setId(UUID.randomUUID());
      image.setContent(dto.getContent());
      image.setThisNew(true);

      return imageRepository.add(image);
    }
    
  
}
