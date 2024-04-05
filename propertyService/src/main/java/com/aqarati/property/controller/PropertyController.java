package com.aqarati.property.controller;

import com.aqarati.property.model.Property;
import com.aqarati.property.request.CreatePropertyRequest;
import com.aqarati.property.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.aqarati.property.exception.InvalidJwtAuthenticationException;

import java.util.List;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
public class PropertyController {
    private final PropertyService propertyService;
    @PostMapping({"/",""})
    public Property createProperty(HttpServletRequest request, @RequestBody CreatePropertyRequest propertyRequest) throws InvalidJwtAuthenticationException {
        return propertyService.createProperty(request,propertyRequest);
    }

    @GetMapping({"/",""})
    public List<Property> getAllProperty(){
        return propertyService.getAll();
    }

}
