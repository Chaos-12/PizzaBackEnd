package com.example.demo.Infrastructure.ImageRepository;

import com.example.demo.domain.ImageDomain.Image;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ImageRepository  extends ReactiveCrudRepository<Image, Integer>{
    
}
