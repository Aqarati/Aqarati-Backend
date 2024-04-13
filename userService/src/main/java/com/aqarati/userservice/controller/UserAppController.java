package com.aqarati.userservice.controller;

import com.aqarati.userservice.request.UserUpdateRequest;
import com.aqarati.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.aqarati.userservice.exception.InvalidJwtAuthenticationException;
import com.aqarati.userservice.model.UserApp;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAppController {
    private final UserService userService;

    @GetMapping("/profile")
    public UserApp getUserInformation(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        return userService.getInformaiton(request);
    }

    @PutMapping("/profile")
    public UserApp updateUserInformation(HttpServletRequest request, @RequestBody UserUpdateRequest userUpdateRequest) throws InvalidJwtAuthenticationException {
        return userService.updateUser(request,userUpdateRequest);
    }
    @PutMapping("/profile/image")
    public UserApp updateUserImage(HttpServletRequest request, @RequestParam("profile-image")MultipartFile image) throws  InvalidJwtAuthenticationException {
        return userService.updateUserImage(request,image);
    }
}

