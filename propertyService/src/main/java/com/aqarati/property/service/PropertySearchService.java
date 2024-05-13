package com.aqarati.property.service;

import com.aqarati.property.model.ElasticProperty;
import com.aqarati.property.repository.ElasticPropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PropertySearchService {
    private final ElasticPropertyRepository elasticPropertyRepository;

    public List<ElasticProperty> searchPropertyByKeyword(String keyword){
        return elasticPropertyRepository.searchPropertyByKeyword(keyword);
    }
}
