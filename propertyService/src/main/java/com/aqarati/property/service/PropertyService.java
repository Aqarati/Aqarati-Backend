package com.aqarati.property.service;

import com.aqarati.property.client.ImageServiceClient;
import com.aqarati.property.exception.NotFoundException;
import com.aqarati.property.model.PropertyImage;
import com.aqarati.property.repository.PropertyImageRepository;
import com.aqarati.property.repository.PropertyRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.aqarati.property.exception.InvalidJwtAuthenticationException;
import com.aqarati.property.model.Property;
import com.aqarati.property.request.CreatePropertyRequest;
import com.aqarati.property.util.JwtTokenUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.Comparator;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
//    private final ElasticPropertyRepository elasticPropertyRepository;
    private final PropertyImageRepository propertyImageRepository;
    private final ImageServiceClient imageServiceClient;
    private final JwtTokenUtil jwtTokenUtil;


    @Cacheable(value = "Properties")
    public List<Property> getAll(){
      var x= propertyRepository.findAll();
        System.out.println("\n \n ");
        System.out.println(x.toString());
        return (List<Property>) x;
    }
    @Cacheable(value = "Properties")
    public List<Property> getAllSortedByCreatedTime(){
        var properties = propertyRepository.findAll();
        properties=(List<Property>) properties;
        ((List<Property>) properties).sort(Comparator.comparing(Property::getCreatedTime));
        return (List<Property>) properties;
    }
    @Cacheable(value = "Properties")
    public List<Property> getAllSortedByPrice(){
        var properties = propertyRepository.findAll();
        properties=(List<Property>) properties;
        ((List<Property>) properties).sort(Comparator.comparing(Property::getPrice));
        return (List<Property>) properties;
    }
    @Cacheable(value = "Properties", key = "#id")
    public Property getPropertyById(Long id) throws NotFoundException{
        return propertyRepository.findById(id).orElseThrow(()->new NotFoundException("Not Found"));
    }
    @CacheEvict(value = "Properties", allEntries = true)
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
    @CachePut(cacheNames = "Properties", key = "#property.id")
    public Property updateProperty(HttpServletRequest request, Property property) throws InvalidJwtAuthenticationException, NotFoundException {
        var token = jwtTokenUtil.resolveToken(request);
        if (!jwtTokenUtil.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid jwt");
        }
        var p =propertyRepository.findById(property.getId()).orElseThrow(()-> new NotFoundException("property with that id not found"));
        if(!(jwtTokenUtil.getUserId(token).equals(p.getUserId()))){
            throw new NotFoundException("property does not belong to that user");
        }
        p.setName((property.getName()!=null)? property.getName():p.getName());
        p.setPrice((property.getPrice() != null)? property.getPrice():p.getPrice());
        p.setDescription((property.getDescription() != null)? property.getDescription():p.getDescription());
        return propertyRepository.save(p);
    }
    @CacheEvict(cacheNames = "Properties", key = "#propertyId")
    public Property deleteProperty (HttpServletRequest request,Long propertyId) throws InvalidJwtAuthenticationException,NotFoundException{
        var token = jwtTokenUtil.resolveToken(request);
        if (!jwtTokenUtil.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("invalid jwt");
        }
        var p =propertyRepository.findById(propertyId).orElseThrow(()-> new NotFoundException("property with that id not found"));
        if(!(jwtTokenUtil.getUserId(token).equals(p.getUserId()))){
            throw new NotFoundException("property does not belong to that user");
        }
        propertyRepository.delete(p);
        return p;
    }
//    @CachePut(cacheNames = "Properties", key = "#propertyId")
    @CacheEvict(value = "Properties", allEntries = true)
    public Property updatePropertyImage(HttpServletRequest request,List<MultipartFile> propertyImages,long propertyId) throws InvalidJwtAuthenticationException,NotFoundException {
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
            propertyImageRepository.save(propertyImage);
            String imageUrl = null;
            try {
                System.out.println("============================ \n");
                imageUrl = imageServiceClient.uploadImage(image, "property-image/%s".formatted(propertyId), String.valueOf(propertyImage.getId()));
                System.out.println(imageUrl);
                System.out.println(property);
                System.out.println(propertyImage);
                propertyImage.setImgUrl(imageUrl);
                propertyImageRepository.save(propertyImage);
            } catch (Exception ex) {
                ex.printStackTrace();
                propertyImageRepository.deleteById(propertyImage.getId());
            }
        }
            return property;
        }
    @CacheEvict(value = "Properties", allEntries = true)
    public PropertyImage activatePropertyImageVr(Long imageId) throws NotFoundException {
        var image = propertyImageRepository.findById(imageId).orElseThrow(()->new NotFoundException("property with that id not found"));
        image.setVr(true);
        int lastSlashIndex = image.getImgUrl().lastIndexOf('/');
        String fileName = image.getImgUrl().substring(lastSlashIndex + 1);
        String imageUrl="https://master.d10hk0k70g00y4.amplifyapp.com/"+image.getProperty().getId()+"/"+fileName;
        image.setVr_url(imageUrl);
        return propertyImageRepository.save(image);
    }
    @CacheEvict(value = "Properties", allEntries = true)
    public PropertyImage deactivatePropertyImageVr(Long imageId) throws NotFoundException {
        var image = propertyImageRepository.findById(imageId).orElseThrow(()->new NotFoundException("property with that id not found"));
        image.setVr(false);
        int lastSlashIndex = image.getImgUrl().lastIndexOf('/');
        String fileName = image.getImgUrl().substring(lastSlashIndex + 1);
        String imageUrl="https://master.d10hk0k70g00y4.amplifyapp.com/"+image.getProperty().getId()+"/"+fileName;
        image.setVr_url("");
        return propertyImageRepository.save(image);
    }

    public  List<Property> getPropertiesById(List<Long>propertiesId){
        return (List<Property>) propertyRepository.findAllById(propertiesId);
    }
    public List<Property> getAllByUserId(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            return propertyRepository.findAllByUserId(jwtTokenUtil.getUserId(token));
        }
        throw new InvalidJwtAuthenticationException("invalid jwt");
    }
    @Transactional
//    @CacheEvict(value = "Properties", allEntries = true)
    public PropertyImage deletePropertyImage(HttpServletRequest request, Long propertyImageId)
            throws InvalidJwtAuthenticationException, NotFoundException {
        var token = jwtTokenUtil.resolveToken(request);
        if (!jwtTokenUtil.validateToken(token)) {
            throw new InvalidJwtAuthenticationException("Invalid JWT");
        }
        var propertyImage = propertyImageRepository.findById(propertyImageId)
                .orElseThrow(() -> new NotFoundException("Property image with that ID not found"));

        // Ensure that the property image belongs to the user making the request
        var userIdFromToken = jwtTokenUtil.getUserId(token);
        var property = propertyImage.getProperty();
        if (!property.getUserId().equals(userIdFromToken)) {
            throw new NotFoundException("Property image does not belong to the user");
        }

        log.info("Deleting Property Image");
        log.info(propertyImage.toString());
        property.getPropertyImages().remove(propertyImage);
        propertyRepository.save(property);
        propertyImageRepository.deleteById(propertyImage.getId());

        boolean exists = propertyImageRepository.existsById(propertyImage.getId());
        log.info("PropertyImage exists after deletion: " + exists);
        return propertyImage;
    }
}