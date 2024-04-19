package com.aqarati.user.service;


import com.aqarati.user.exception.InvalidImageException;
import com.aqarati.user.publisher.Publisher;
import com.aqarati.user.request.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.aqarati.user.client.ImageServiceClient;
import com.aqarati.user.exception.InvalidJwtAuthenticationException;
import com.aqarati.user.model.UserApp;
import com.aqarati.user.repository.UserRepository;
import com.aqarati.user.util.JwtTokenUtil;

import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageServiceClient imageServiceClient;
    private final JwtTokenUtil jwtTokenUtil;
    private final Publisher publisher;
    private final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png");


    public UserApp getInformaiton(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        var userEmail = jwtTokenUtil.getEmail(token);
        if (jwtTokenUtil.validateToken(token)) {
            return userRepository.findByEmail(userEmail).orElseThrow();
        }
        throw new InvalidJwtAuthenticationException("invalid JWT ");
    }

    public UserApp updateUser(HttpServletRequest request, UserUpdateRequest userUpdateRequest) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        var userEmail = jwtTokenUtil.getEmail(token);
        if (jwtTokenUtil.validateToken(token)) {
            var x = userRepository.findByEmail(userEmail).orElseThrow();
            x.setFirstName(userUpdateRequest.getFirstName());
            x.setLastName(userUpdateRequest.getLastName());
            return userRepository.save(x);
        }
        throw new InvalidJwtAuthenticationException("invalid JWT");
    }
    public UserApp updateUserImage(HttpServletRequest request, MultipartFile image) throws InvalidJwtAuthenticationException,InvalidImageException {
        var token = jwtTokenUtil.resolveToken(request);
        var userEmail = jwtTokenUtil.getEmail(token);
        if (jwtTokenUtil.validateToken(token)) {
            if (image.isEmpty()) {
                throw new InvalidImageException("Please select a file to upload");
            }

            if (image.getSize() > 15 * 1024 * 1024) { // 15MB
                throw new InvalidImageException("File size exceeds the limit of 15MB");
            }

            if (!ALLOWED_IMAGE_TYPES.contains(image.getContentType())) {
                throw new InvalidImageException("Only JPEG and PNG images are allowed");
            }
            var x = userRepository.findByEmail(userEmail).orElseThrow();
//            var imageUrl =imageServiceClient.uploadImage(image,"profile-image",x.getId());
            publisher.publishImageChunks(image,"profile-image",x.getId());
//            x.setImageUrl(imageUrl);
            return userRepository.save(x);
        }
        throw new InvalidJwtAuthenticationException("invalid JWT");
    }


}
