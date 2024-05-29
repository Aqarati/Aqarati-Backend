package com.aqarati.document.controller;

import com.aqarati.document.exception.NotFoundException;
import com.aqarati.document.exception.UnAuthorizedAccessException;
import com.aqarati.document.model.Document;
import com.aqarati.document.model.DocumentStatus;
import com.aqarati.document.request.ChangeDocumentStatusRequest;
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
    @GetMapping("/{id}")
    public List<Document> getPropertyDocument(HttpServletRequest request,@PathVariable String id) throws InvalidJwtAuthenticationException {
        return documentService.getPropertyDocument(request,id);
    }

    @PostMapping({"/",""})
    public Document createDocument(HttpServletRequest request, @RequestParam(name = "image") MultipartFile documnetImage,@RequestParam("property-id")String propertyId) throws InvalidJwtAuthenticationException, NotFoundException,Exception {
        return documentService.createDocument(request,documnetImage,propertyId);
    }

    @PutMapping({"/",""})
    public Document updateDocument(HttpServletRequest request, @RequestParam(name = "image") MultipartFile documnetImage,@RequestParam("property-id")String propertyId,@RequestParam("document-id") String documentId) throws InvalidJwtAuthenticationException, NotFoundException,Exception {
        return documentService.updateDocument(request,documnetImage,propertyId,documentId);
    }

    @GetMapping("/admin")
    public List<Document> adminGetAllDocument(HttpServletRequest request, @RequestParam(name = "filter",required = false) DocumentStatus documentStatus) throws InvalidJwtAuthenticationException, UnAuthorizedAccessException {
        return documentService.adminGetAllDocument(request,documentStatus);
    }

    @PutMapping("/admin")
    public Document adminChangeDocumentStatus(HttpServletRequest request, @RequestBody ChangeDocumentStatusRequest changeDocumentStatusRequest) throws InvalidJwtAuthenticationException, UnAuthorizedAccessException, NotFoundException {
        return documentService.adminChangeDocumentStatus(request,changeDocumentStatusRequest);
    }

    @DeleteMapping("/admin")
    public Document adminDeleteDocument(HttpServletRequest request, @RequestParam("id") Long id) throws InvalidJwtAuthenticationException, UnAuthorizedAccessException, NotFoundException {
        return documentService.adminDeleteDocument(request,id);
    }
}
