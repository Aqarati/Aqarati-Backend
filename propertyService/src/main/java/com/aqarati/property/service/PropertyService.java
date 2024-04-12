package com.aqarati.property.service;

import com.aqarati.property.client.ImageServiceClient;
import com.aqarati.property.exception.NotFoundException;
import com.aqarati.property.model.PropertyImage;
import com.aqarati.property.repository.PropertyImageRepositorty;
import com.aqarati.property.repository.PropertyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aqarati.property.exception.InvalidJwtAuthenticationException;
import com.aqarati.property.model.Property;
import com.aqarati.property.request.CreatePropertyRequest;
import com.aqarati.property.util.JwtTokenUtil;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyImageRepositorty propertyImageRepositorty;
    private final ImageServiceClient imageServiceClient;
    private final JwtTokenUtil jwtTokenUtil;

    public List<Property> getAll(){

      var x= propertyRepository.findAll();
        System.out.println("\n \n ");
        System.out.println(x.toString());
        return x;
    }

    public Property createProperty(HttpServletRequest request, CreatePropertyRequest propertyRequest) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            var property= Property.builder().
                    name(propertyRequest.getName()).
                    description(propertyRequest.getDescription()).
                    price(propertyRequest.getPrice()).
                    userId(jwtTokenUtil.getUserId(token)).
                    build();
            return propertyRepository.save(property);
            }
        throw new InvalidJwtAuthenticationException("invalid jwt");
    }

    public Property updatePropertyImage(HttpServletRequest request, @RequestParam(name = "image") List<MultipartFile> propertyImages, @RequestParam("property-id")String propertyId) throws InvalidJwtAuthenticationException,NotFoundException {
        var token = jwtTokenUtil.resolveToken(request);
        if (!jwtTokenUtil.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid jwt");
        }
        var property =propertyRepository.findById(propertyId).orElseThrow(()-> new NotFoundException("property with that id not found"));
        if(!(jwtTokenUtil.getUserId(token).equals(property.getUserId()))){
            throw new NotFoundException("property does not belong to that user");
        }
        for (var image : propertyImages) {
            var propertyImage = PropertyImage.builder().property(property).build();
            propertyImageRepositorty.save(propertyImage);
            String imageUrl = null;
            try {
                System.out.println("============================ \n");
                imageUrl = imageServiceClient.uploadImage(image, "property-image/%s".formatted(propertyId), String.valueOf(propertyImage.getId()));
                System.out.println(imageUrl);
                System.out.println(property);
                System.out.println(propertyImage);
                propertyImage.setImgUrl(imageUrl);
                propertyImageRepositorty.save(propertyImage);
            } catch (Exception ex) {
                ex.printStackTrace();
                propertyImageRepositorty.deleteById(propertyImage.getId());
            }
        }
            return property;
        }
    public PropertyImage activatePropertyImageVr(Long imageId) throws NotFoundException {
        var image =propertyImageRepositorty.findById(imageId).orElseThrow(()->new NotFoundException("property with that id not found"));
        image.setVr(true);
        int lastSlashIndex = image.getImgUrl().lastIndexOf('/');
        String fileName = image.getImgUrl().substring(lastSlashIndex + 1);
        String imageUrl="https://master.d10hk0k70g00y4.amplifyapp.com/"+image.getProperty().getId()+"/"+fileName;
        image.setVr_url(imageUrl);
        return propertyImageRepositorty.save(image);
    }
}