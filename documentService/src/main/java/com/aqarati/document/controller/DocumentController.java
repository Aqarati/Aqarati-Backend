package com.aqarati.document.controller;

import com.aqarati.document.exception.PropertyNotFoundException;
import com.aqarati.document.model.Document;
import com.aqarati.document.request.CreateDocumentRequest;
import com.aqarati.document.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.aqarati.document.exception.InvalidJwtAuthenticationException;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/document")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping({"/",""})
    public Document createDocument(HttpServletRequest request, @RequestParam(name = "image") MultipartFile documnetImage,@RequestParam("property-id")String propertyId) throws InvalidJwtAuthenticationException, PropertyNotFoundException {
        return documentService.createDocument(request,documnetImage,propertyId);
    }
}
