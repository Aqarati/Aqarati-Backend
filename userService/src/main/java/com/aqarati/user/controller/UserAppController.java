package com.aqarati.user.controller;

import com.aqarati.user.exception.InvalidImageException;
import com.aqarati.user.request.UserUpdateRequest;
import com.aqarati.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.aqarati.user.exception.InvalidJwtAuthenticationException;
import com.aqarati.user.model.UserApp;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserAppController {
    private final UserService userService;

    @GetMapping("/profile")
    public UserApp getUserInformation(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        return userService.getInformation(request);
    }
    @GetMapping("/profile/{id}")
    public UserApp getUserInformation(@PathVariable String id) throws InvalidJwtAuthenticationException {
        return userService.getInformation(id);
    }
    @GetMapping("/favourite")
    public List<Long> getUserFavouriteProeperty(HttpServletRequest request) throws InvalidJwtAuthenticationException {
        return userService.getUserFavouriteProeperty(request);
    }
    @PostMapping("/favourite")
    public List<Long> saveUserFavouriteProeperty(HttpServletRequest request,@RequestParam Long id) throws InvalidJwtAuthenticationException {
        return userService.saveUserFavouriteProperty(request,id);
    }
    @DeleteMapping("/favourite")
    public List<Long> deleteUserFavouriteProeperty(HttpServletRequest request,@RequestParam Long id) throws InvalidJwtAuthenticationException {
        return userService.deleteUserFavouriteProperty(request,id);
    }
    @PutMapping("/profile")
    public UserApp updateUserInformation(HttpServletRequest request, @RequestBody UserUpdateRequest userUpdateRequest) throws InvalidJwtAuthenticationException {
        return userService.updateUser(request,userUpdateRequest);
    }
    @PutMapping("/profile/image")
    public UserApp updateUserImage(HttpServletRequest request, @RequestParam("profile-image")MultipartFile image) throws InvalidJwtAuthenticationException, InvalidImageException {
        return userService.updateUserImage(request,image);
    }
}

