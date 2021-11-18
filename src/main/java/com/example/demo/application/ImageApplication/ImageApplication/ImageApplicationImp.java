package com.example.demo.application.ImageApplication.ImageApplication;

import java.io.IOException;

import com.example.demo.Infrastructure.ImageRepository.ImageRepository;
import com.example.demo.Infrastructure.ImageRepository.ImageRepositoryImp;
import com.example.demo.domain.Image;

public class ImageApplicationImp implements ImageApplication {
    private final ImageRepository imageRepository;

    public ImageApplicationImp(final ImageRepository imageRepository){
        this.imageRepository = imageRepository;
    }

    @Override
    public ImageDTO save(CreateOrUpdateImageDTO dto){
    //   Image image = modelMapper.map(dto, Image.class)

        return null;
    }
}
