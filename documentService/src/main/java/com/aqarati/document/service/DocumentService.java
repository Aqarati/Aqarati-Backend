package com.aqarati.document.service;

import com.aqarati.document.client.ImageServiceClient;
import com.aqarati.document.exception.PropertyNotFoundException;
import com.aqarati.document.repository.DocumentRepository;
import com.aqarati.document.repository.PropertyRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.aqarati.document.exception.InvalidJwtAuthenticationException;
import com.aqarati.document.model.Document;
import com.aqarati.document.util.JwtTokenUtil;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final PropertyRepository propertyRepository;
    private final JwtTokenUtil jwtTokenUtil;
    private final ImageServiceClient imageServiceClient;

    public Document createDocument(HttpServletRequest request, MultipartFile documnetImage,String propertyId) throws InvalidJwtAuthenticationException,PropertyNotFoundException {
        var token = jwtTokenUtil.resolveToken(request);
        if (jwtTokenUtil.validateToken(token)) {
            var property =propertyRepository.findById(propertyId).orElseThrow(()-> new PropertyNotFoundException("property with that id not found"));
            if(!(jwtTokenUtil.getUserId(token).equals(property.getUserId()))){
                throw new PropertyNotFoundException("prproperty doent belong to that user");
            }
            var imageUrl =imageServiceClient.uploadImage(documnetImage,"document-image",propertyId);
            var document= Document.builder().
                    imgUrl(imageUrl).userId(jwtTokenUtil.getUserId(token)).
                    propertyId(propertyId).build();
            return documentRepository.save(document);
            }
        throw new InvalidJwtAuthenticationException("");
    }
}