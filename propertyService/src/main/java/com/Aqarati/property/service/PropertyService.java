package com.Aqarati.property.service;

import com.Aqarati.property.repository.PropertyRepository;
import com.Aqarati.property.repository.SiteRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Aqarati.property.exception.InvalidJwtAuthenticationException;
import com.Aqarati.property.model.Property;
import com.Aqarati.property.model.Site;
import com.Aqarati.property.request.CreatePropertyRequest;
import com.Aqarati.property.util.JwtTokenUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final SiteRepository siteRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public List<Property> getAll(){
      return propertyRepository.findAll();
    }
//    public Property createProduct(HttpServletRequest request, CreatePropertyRequest productRequest, Long siteId) throws InvalidJwtAuthenticationException {
//        var token = jwtTokenUtil.resolveToken(request);
//        var userId = jwtTokenUtil.getUserId(token);
//        //Todo: check if the user have that site
//        var userSite=siteRepository.findAllByUserId(userId);
//        boolean contain=false;
//        for (Site site : userSite) {
//            if (site.getId().equals(siteId))contain=true;
//        }
//
//        if (contain){
//        if (jwtTokenUtil.validateToken(token)) {
//            var product= Property.builder().
//                    name(productRequest.getName()).
//                    description(productRequest.getDescription()).
//                    price(productRequest.getPrice()).
//                    siteID(siteId).
//                    build();
//            return propertyRepository.save(product);
//            }
//        }
//        return null;
//    }
}