package com.aqarati.property.controller;

import com.aqarati.property.exception.NotFoundException;
import com.aqarati.property.model.Property;
import com.aqarati.property.model.PropertyImage;
import com.aqarati.property.repository.PropertyImageRepository;
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
    private final PropertyImageRepository propertyImageRepository;

    @GetMapping({"/",""})
    public List<Property> getAllProperty(@RequestParam(required = false) String sortBy) {
        if (sortBy != null && !sortBy.isEmpty()) {
            if (sortBy.equalsIgnoreCase("createdTime")) {
                return propertyService.getAllSortedByCreatedTime();
            } else if (sortBy.equalsIgnoreCase("price")) {
                return propertyService.getAllSortedByPrice();
            }
        }
        // Default behavior: Return all properties without sorting
        return propertyService.getAll();
    }

    @GetMapping("/my")
    public List<Property> getAllProperty(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        return propertyService.getAllByUserId(request);
    }

    @GetMapping("/{id}")
    public Property getPropertyById(@PathVariable("id") Long id) throws NotFoundException {
        return propertyService.getPropertyById(id);
    }
    @GetMapping("/properties")
    public List<Property> getPropertiesById(@RequestParam List<Long>  PropertiesIDs) throws NotFoundException {
        return propertyService.getPropertiesById(PropertiesIDs);
    }

    @PostMapping({"/",""})
    public Property createProperty(HttpServletRequest request, @RequestBody CreatePropertyRequest propertyRequest) throws InvalidJwtAuthenticationException {
        return propertyService.createProperty(request,propertyRequest);
    }

    @PutMapping("/")
    public Property updateProperty(HttpServletRequest request,@RequestBody Property property) throws InvalidJwtAuthenticationException, NotFoundException {
        return propertyService.updateProperty(request,property);
    }

    @DeleteMapping("/{property-id}")
    public Property deleteProperty (HttpServletRequest request,@PathVariable("property-id") Long propertyId) throws InvalidJwtAuthenticationException,NotFoundException {
        return propertyService.deleteProperty(request,propertyId);
    }

    @PutMapping("/image/{property-id}")
    public Property updatePropertyImage(HttpServletRequest request, @RequestParam(name = "images") List<MultipartFile> propertyImages, @PathVariable("property-id")Long propertyId) throws InvalidJwtAuthenticationException, NotFoundException {
        return propertyService.updatePropertyImage(request,propertyImages,propertyId);
    }

    @GetMapping("/image")
    public List<PropertyImage> GetPropertyImage(){
        return propertyImageRepository.findAll();
    }

    @PutMapping("/image/active/{id}")
    public PropertyImage activatePropertyImageVr(@PathVariable("id")Long imageId) throws NotFoundException {
        return propertyService.activatePropertyImageVr(imageId);
    }
    @PutMapping("/image/deactive/{id}")
    public PropertyImage deactivatePropertyImageVr(@PathVariable("id")Long imageId) throws NotFoundException {
        return propertyService.deactivatePropertyImageVr(imageId);
    }
    @DeleteMapping("/image/{id}")
    public PropertyImage deletePropertyImage(HttpServletRequest request,@PathVariable Long id) throws NotFoundException, InvalidJwtAuthenticationException {
        return propertyService.deletePropertyImage(request,id);
    }

}
