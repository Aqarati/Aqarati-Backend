package com.aqarati.document.service;

import com.aqarati.document.client.ImageServiceClient;
import com.aqarati.document.exception.NotFoundException;
import com.aqarati.document.exception.UnAuthorizedAccessException;
import com.aqarati.document.model.DocumentStatus;
import com.aqarati.document.repository.DocumentRepository;
import com.aqarati.document.repository.PropertyRepository;
import com.aqarati.document.request.ChangeDocumentStatusRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aqarati.document.exception.InvalidJwtAuthenticationException;
import com.aqarati.document.model.Document;
import com.aqarati.document.util.JwtTokenUtil;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final PropertyRepository propertyRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ImageServiceClient imageServiceClient;

    public Document createDocument(HttpServletRequest request, MultipartFile documnetImage,String propertyId) throws Exception {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            var property =propertyRepository.findById(propertyId).orElseThrow(()-> new NotFoundException("property with that id not found"));
            if(!(jwtTokenUtil.getUserId(token).equals(property.getUserId()))){
                throw new NotFoundException("property does not belong to that user");
            }
            var document= Document.builder().
                    userId(jwtTokenUtil.getUserId(token)).
                    propertyId(propertyId).build();
            documentRepository.save(document);
            String imageUrl;
            try {
                imageUrl=imageServiceClient.uploadImage(documnetImage,"document-image/%s".formatted(propertyId), String.valueOf(document.getId()));
            }catch (Exception e){
                documentRepository.deleteById(document.getId());
                throw new Exception(e.getMessage());
            }
            document.setImgUrl(imageUrl);
            return documentRepository.save(document);
            }
        throw new InvalidJwtAuthenticationException("");
    }

    public List<Document> getUserDocument(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            return documentRepository.findAllByUserId(jwtTokenUtil.getUserId(token));
        }
        throw new InvalidJwtAuthenticationException("");
    }

    public Document updateDocument(HttpServletRequest request, MultipartFile documnetImage,String propertyId, String documentId) throws InvalidJwtAuthenticationException, NotFoundException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            var property =propertyRepository.findById(propertyId).orElseThrow(()-> new NotFoundException("property with that id not found"));
            if(!(jwtTokenUtil.getUserId(token).equals(property.getUserId()))){
                throw new NotFoundException("property does not belong to that user");
            }
            var document= documentRepository.findById(Long.valueOf(documentId)).orElseThrow(()->new NotFoundException(" document Not found"));
            if(documentId.equals(document.getUserId())){
                throw new NotFoundException("document does not belong to that user");
            }
            var imageUrl=imageServiceClient.uploadImage(documnetImage,"document-image/%s".formatted(propertyId), String.valueOf(document.getId()));
            document.setImgUrl(imageUrl);
            return documentRepository.save(document);
        }
        throw new InvalidJwtAuthenticationException("");
    }

    public List<Document> adminGetAllDocument(HttpServletRequest request)throws InvalidJwtAuthenticationException, UnAuthorizedAccessException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            if (jwtTokenUtil.isUserAdmin(token)){
            return documentRepository.findAll();
            }
            throw new UnAuthorizedAccessException("you not allowed to access");
        }
        throw new InvalidJwtAuthenticationException("");
    }

    public Document adminChangeDocumentStatus(HttpServletRequest request, ChangeDocumentStatusRequest changeDocumentStatusRequest) throws InvalidJwtAuthenticationException, UnAuthorizedAccessException, NotFoundException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            if (jwtTokenUtil.isUserAdmin(token)){
                var doc =documentRepository.findById(changeDocumentStatusRequest.getId()).orElseThrow(()->new NotFoundException("document with that id not found "));
                switch (changeDocumentStatusRequest.getStatus()){
                    case APPROVED -> doc.setStatus(DocumentStatus.APPROVED);
                    case PENDING -> doc.setStatus(DocumentStatus.PENDING);
                    case DENIED -> doc.setStatus(DocumentStatus.DENIED);
                }
                return documentRepository.save(doc);
            }
            throw new UnAuthorizedAccessException("you not allowed to access");
        }
        throw new InvalidJwtAuthenticationException("");
    }

    public Document adminDeleteDocument(HttpServletRequest request, Long id) throws InvalidJwtAuthenticationException, UnAuthorizedAccessException, NotFoundException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            if (jwtTokenUtil.isUserAdmin(token)){
                var doc =documentRepository.findById(id).orElseThrow(()->new NotFoundException("document with that id not found "));
                documentRepository.deleteById(id);
                return doc;
            }
            throw new UnAuthorizedAccessException("you not allowed to access");
        }
        throw new InvalidJwtAuthenticationException("");
    }
}