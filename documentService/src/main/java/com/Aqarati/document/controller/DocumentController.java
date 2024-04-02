package com.Aqarati.document.controller;

import com.Aqarati.document.model.Document;
import com.Aqarati.document.request.CreateDocumentRequest;
import com.Aqarati.document.service.DocumentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import com.Aqarati.document.exception.InvalidJwtAuthenticationException;

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
