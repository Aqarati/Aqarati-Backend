package com.ahmadah.imageservice.controller;

import com.ahmadah.imageservice.exception.InvalidImageException;
import com.ahmadah.imageservice.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
public class ImageController {

    private final ImageService imageService;

    @GetMapping({"/image","/image/"})
    public String getImage(@RequestParam("key") String key){
        return imageService.getObject(key);
    }
    @PostMapping("/image/upload")
    public String  uploadImage(@RequestParam("image") MultipartFile image,@RequestParam("folder") String folderName,@RequestParam("name") String imageName) throws InvalidImageException {
        return imageService.putObject(image,folderName,imageName);
    }
}
