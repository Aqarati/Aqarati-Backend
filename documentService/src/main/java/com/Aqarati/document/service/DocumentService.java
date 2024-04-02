package com.Aqarati.document.service;

import com.Aqarati.document.repository.DocumentRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.Aqarati.document.exception.InvalidJwtAuthenticationException;
import com.Aqarati.document.model.Document;
import com.Aqarati.document.request.CreateDocumentRequest;
import com.Aqarati.document.util.JwtTokenUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final JwtTokenUtil jwtTokenUtil;

    public List<Document> getAll(){
      return documentRepository.findAll();
    }
    public Document createDocument(HttpServletRequest request, CreateDocumentRequest propertyRequest) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        var userId = jwtTokenUtil.getUserId(token);
//        if (jwtTokenUtil.validateToken(token)) {
//            var document= Document.builder().
//            return documentRepository.save(product);
//            }
        return null;
    }
}