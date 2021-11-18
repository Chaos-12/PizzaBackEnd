package com.example.demo.controller;

import java.io.IOException;

import com.example.demo.application.ImageApplication.ImageApplication.CreateOrUpdateImageDTO;
import com.example.demo.application.ImageApplication.ImageApplication.ImageApplicationImp;
import com.example.demo.application.ImageApplication.ImageApplication.ImageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/images")
public class ImageController {

    private final ImageApplicationImp imageApplicationImp;

    @Autowired
    public ImageController(final ImageApplicationImp imageApplicationImpl) {
        this.imageApplicationImp = imageApplicationImpl;
    }    

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> upload(@RequestParam("image") MultipartFile file) throws IOException {

        CreateOrUpdateImageDTO dto = new CreateOrUpdateImageDTO();
        dto.setContent(file.getBytes());
        ImageDTO imageDTO = imageApplicationImp.save(dto);
        return ResponseEntity.status(201).body(imageDTO);
    }
    


}
