package com.Aqarati.property.controller;

import com.Aqarati.property.model.Property;
import com.Aqarati.property.request.CreatePropertyRequest;
import com.Aqarati.property.service.PropertyService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.Aqarati.property.exception.InvalidJwtAuthenticationException;

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
