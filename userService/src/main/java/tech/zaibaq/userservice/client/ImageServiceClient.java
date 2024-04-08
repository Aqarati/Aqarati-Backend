package tech.zaibaq.userservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "image-service")
public interface ImageServiceClient {

    @PostMapping(value = "/image/upload",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadImage(@RequestPart("image") MultipartFile image,
                       @RequestPart("folder") String folderName,
                       @RequestPart("name") String imageName);
}
