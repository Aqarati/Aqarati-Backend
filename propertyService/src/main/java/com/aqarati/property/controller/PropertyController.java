package com.aqarati.property.controller;

import com.aqarati.property.exception.NotFoundException;
import com.aqarati.property.model.Property;
import com.aqarati.property.model.PropertyImage;
import com.aqarati.property.repository.PropertyImageRepositorty;
import com.aqarati.property.request.CreatePropertyRequest;
import com.aqarati.property.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.aqarati.property.exception.InvalidJwtAuthenticationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;
    private final PropertyImageRepositorty propertyImageRepositorty;

    
    @GetMapping({"/",""})
    public List<Property> getAllProperty(){
        return propertyService.getAll();
    }

    @PostMapping({"/",""})
    public Property createProperty(HttpServletRequest request, @RequestBody CreatePropertyRequest propertyRequest) throws InvalidJwtAuthenticationException {
        return propertyService.createProperty(request,propertyRequest);
    }

    @PutMapping("/")
    public Property updateProperty(HttpServletRequest request,@RequestBody Property property) throws InvalidJwtAuthenticationException, NotFoundException {
        return propertyService.updateProperty(request,property);
    }

    @DeleteMapping("{id}")
    public Property deleteProperty (HttpServletRequest request,@PathVariable("id") Long propertyId) throws InvalidJwtAuthenticationException,NotFoundException {
        return propertyService.deleteProperty(request,propertyId);
    }

    @PutMapping("/image/{property-id}")
    public Property updatePropertyImage(HttpServletRequest request, @RequestParam(name = "images") List<MultipartFile> propertyImages, @PathVariable("property-id")Long propertyId) throws InvalidJwtAuthenticationException, NotFoundException {
        return propertyService.updatePropertyImage(request,propertyImages,propertyId);
    }

    @GetMapping("/image")
    public List<PropertyImage> GetPropertyImage(){
        return propertyImageRepositorty.findAll();
    }

    @PutMapping("/image/active/{id}")
    public PropertyImage activatePropertyImageVr(@PathVariable("id")Long imageId) throws NotFoundException {
        return propertyService.activatePropertyImageVr(imageId);
    }

}
