package com.example.demo.controller;

import java.io.IOException;

import com.example.demo.application.ImageApplication.ImageApplication.CreateOrUpdateImageDTO;
import com.example.demo.application.ImageApplication.ImageApplication.ImageApplicationImp;
import com.example.demo.application.ImageApplication.ImageApplication.ImageDTO;
import com.example.demo.domain.Image;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageApplicationImp imageApplicationImp;
    @Autowired
    public ImageController(final ImageApplicationImp imageApplicationImpl) {
        this.imageApplicationImp = imageApplicationImpl;
    }    

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<Image>  upload(@RequestParam("image") MultipartFile file) throws IOException {
        ImageDTO dto = new ImageDTO();
        dto.setContent(file.getBytes());
        return imageApplicationImp.save(dto);
    }
 
}
