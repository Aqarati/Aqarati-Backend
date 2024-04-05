package com.aqarati.property.service;

import com.aqarati.property.repository.PropertyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aqarati.property.exception.InvalidJwtAuthenticationException;
import com.aqarati.property.model.Property;
import com.aqarati.property.request.CreatePropertyRequest;
import com.aqarati.property.util.JwtTokenUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public List<Property> getAll(){
      return propertyRepository.findAll();
    }
    public Property createProperty(HttpServletRequest request, CreatePropertyRequest propertyRequest) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            var product= Property.builder().
                    name(propertyRequest.getName()).
                    description(propertyRequest.getDescription()).
                    price(propertyRequest.getPrice()).
                    imgUrl(propertyRequest.getImgUrl()).
                    userId(jwtTokenUtil.getUserId(token)).
                    build();
            return propertyRepository.save(product);
            }
        return null;
    }
}