package com.aqarati.document.service;

import com.aqarati.document.client.ImageServiceClient;
import com.aqarati.document.exception.PropertyNotFoundException;
import com.aqarati.document.exception.UnAuthorizedAccessException;
import com.aqarati.document.repository.DocumentRepository;
import com.aqarati.document.repository.PropertyRepository;
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
            var property =propertyRepository.findById(propertyId).orElseThrow(()-> new PropertyNotFoundException("property with that id not found"));
            if(!(jwtTokenUtil.getUserId(token).equals(property.getUserId()))){
                throw new PropertyNotFoundException("property does not belong to that user");
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
    //Todo: check if his admin
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

}