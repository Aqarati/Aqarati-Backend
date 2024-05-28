package com.aqarati.property.controller;


import com.aqarati.property.model.Property;
import com.aqarati.property.repository.PropertyImageRepository;
import com.aqarati.property.service.PropertySearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/property/search")
@RequiredArgsConstructor
public class PropertySearchController {
    private final PropertySearchService propertySearchService;
    private final PropertyImageRepository propertyImageRepository;

//    @GetMapping("/")
//    public List<ElasticProperty> searchPropertyByKeyword(@RequestParam String keyword){
//        return propertySearchService.searchPropertyByKeyword(keyword);
//    }
    @GetMapping("/")
    public List<Property> searchPropertyByKeyword(@RequestParam String keyword){
        return propertySearchService.searchPropertyByKeyword(keyword);
    }


}
