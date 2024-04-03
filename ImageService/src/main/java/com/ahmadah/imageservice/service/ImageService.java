package com.ahmadah.imageservice.service;

import com.ahmadah.imageservice.exception.InvalidImageException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final AmazonS3 amazonS3;

    @Value("${bucket.name}")
    private String bucketName;

    private final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png");

    public String putObject(MultipartFile image,String folderName,String imageName)throws InvalidImageException{
        if (image.isEmpty()) {
            throw new InvalidImageException("Please select a file to upload");
        }

        if (image.getSize() > 15 * 1024 * 1024) { // 15MB
            throw new InvalidImageException("File size exceeds the limit of 15MB");
        }

        if (!ALLOWED_IMAGE_TYPES.contains(image.getContentType())) {
            throw new InvalidImageException("Only JPEG and PNG images are allowed");
        }
        try {
            String ext="."+image.getContentType().substring(6);
            String key =folderName+"/"+imageName+ext;
            File file = convertMultiPartFileToFile(image);
            amazonS3.putObject(new PutObjectRequest(bucketName, key,  file));
            file.delete(); // Delete the temporary file after uploading
            return "https://aqarati-app.s3.me-south-1.amazonaws.com/"+folderName+"/"+imageName;
        } catch (IOException | SdkClientException e) {
            e.printStackTrace();
            return null;
        }

    };

    //Return the Presigned Url for non public object
    public String getObject(String objectName){
        try {
            Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000); // URL expiration time (1 hour)
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, objectName).withExpiration(expiration);
            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (SdkClientException e) {
            e.printStackTrace();
            return null;
        }
    };

    public File convertMultiPartFileToFile(MultipartFile multipartFile) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        File file = new File(System.currentTimeMillis() + extension); // Using timestamp for unique filename
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(multipartFile.getBytes());
        }
        return file;
    }

}
