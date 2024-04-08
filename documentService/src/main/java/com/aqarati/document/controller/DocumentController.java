package com.aqarati.document.controller;

import com.aqarati.document.exception.PropertyNotFoundException;
import com.aqarati.document.exception.UnAuthorizedAccessException;
import com.aqarati.document.model.Document;
import com.aqarati.document.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.aqarati.document.exception.InvalidJwtAuthenticationException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @GetMapping({"/",""})
    public List<Document> getUserDocument(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        return documentService.getUserDocument(request);
    }

    @PostMapping({"/",""})
    public Document createDocument(HttpServletRequest request, @RequestParam(name = "image") MultipartFile documnetImage,@RequestParam("property-id")String propertyId) throws InvalidJwtAuthenticationException, PropertyNotFoundException,Exception {
        return documentService.createDocument(request,documnetImage,propertyId);
    }

    @GetMapping("/admin")
    public List<Document> adminGetAllDocument(HttpServletRequest request) throws InvalidJwtAuthenticationException, UnAuthorizedAccessException {
        return documentService.adminGetAllDocument(request);
    }
}
