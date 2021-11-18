package com.example.demo.Infrastructure.ImageRepository;

import com.example.demo.domain.ImageDomain.Image;
import com.example.demo.domain.ImageDomain.ImageRepositoryWrite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class ImageRepositoryImp implements ImageRepositoryWrite{

    private final ImageRepository imageRepository;

    @Autowired
    public ImageRepositoryImp(final ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    public void add(Image image){
        imageRepository.save(image);
    }
    
    
}
