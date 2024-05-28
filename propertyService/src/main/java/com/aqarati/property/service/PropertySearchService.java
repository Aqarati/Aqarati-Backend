package com.aqarati.property.service;

import com.aqarati.property.model.ElasticProperty;
import com.aqarati.property.model.Property;
import com.aqarati.property.repository.ElasticPropertyRepository;
import com.aqarati.property.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertySearchService {
//    private final ElasticPropertyRepository elasticPropertyRepository;
    private final PropertyRepository propertyRepository;

//    public List<ElasticProperty> searchPropertyByKeyword(String keyword){
//        return elasticPropertyRepository.searchPropertyByKeyword(keyword);
//    }

    public List<Property> searchPropertyByKeyword(String keyword){
        return propertyRepository.searchByKeyword(keyword);
    }

}
