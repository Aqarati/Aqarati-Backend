package com.aqarati.document.controller;

import com.aqarati.document.model.Document;
import com.aqarati.document.request.CreateDocumentRequest;
import com.aqarati.document.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.aqarati.document.exception.InvalidJwtAuthenticationException;

@RestController
@RequestMapping("/property")
@RequiredArgsConstructor
public class DocumentController {
    private final DocumentService documentService;

    @PostMapping({"/",""})
    public Document createDocument(HttpServletRequest request, @RequestBody CreateDocumentRequest propertyRequest) throws InvalidJwtAuthenticationException {
        return documentService.createDocument(request,propertyRequest);
    }
}
