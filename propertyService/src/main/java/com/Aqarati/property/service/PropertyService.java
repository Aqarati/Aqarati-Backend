package com.Aqarati.property.service;

import com.Aqarati.property.repository.PropertyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Aqarati.property.exception.InvalidJwtAuthenticationException;
import com.Aqarati.property.model.Property;
import com.Aqarati.property.request.CreatePropertyRequest;
import com.Aqarati.property.util.JwtTokenUtil;

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
        var userId = jwtTokenUtil.getUserId(token);
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