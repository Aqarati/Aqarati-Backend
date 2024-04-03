package com.ahmadah.imageservice.controller;

import com.ahmadah.imageservice.exception.InvalidImageException;
import com.ahmadah.imageservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @PostMapping("/image/uploud")
    public String  uploadImage(@RequestParam("file") MultipartFile image,@RequestParam("folder") String folderName,@RequestParam("name") String imageName) throws InvalidImageException {
        return imageService.putObject(image,folderName,imageName);
    }
}
