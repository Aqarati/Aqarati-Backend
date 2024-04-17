package com.aqarati.user.service;


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

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ImageServiceClient imageServiceClient;
    private final JwtTokenUtil jwtTokenUtil;

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
    public UserApp updateUserImage(HttpServletRequest request, MultipartFile file) throws InvalidJwtAuthenticationException {
        var token = jwtTokenUtil.resolveToken(request);
        var userEmail = jwtTokenUtil.getEmail(token);
        if (jwtTokenUtil.validateToken(token)) {
            var x = userRepository.findByEmail(userEmail).orElseThrow();
            var imageUrl =imageServiceClient.uploadImage(file,"profile-image",x.getId());
            x.setImageUrl(imageUrl);
            return userRepository.save(x);
        }
        throw new InvalidJwtAuthenticationException("invalid JWT");
    }


}
