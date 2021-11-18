package com.example.demo.application.ImageApplication.ImageApplication;

import com.example.demo.Infrastructure.ImageRepository.ImageRepository;

import org.springframework.beans.factory.annotation.Autowired;

public interface ImageApplication {
    public ImageDTO save(CreateOrUpdateImageDTO dto);
}
