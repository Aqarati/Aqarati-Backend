package tech.zaibaq.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import tech.zaibaq.userservice.exception.InvalidJwtAuthenticationException;
import tech.zaibaq.userservice.model.UserApp;
import tech.zaibaq.userservice.request.UserUpdateRequest;
import tech.zaibaq.userservice.service.UserService;

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
    public UserApp updateUserInformation(HttpServletRequest request, @RequestBody UserUpdateRequest userUpdateRequest) throws InvalidJwtAuthenticationException, InvalidJwtAuthenticationException {
        return userService.updateUser(request,userUpdateRequest);
    }
    @PutMapping("/profile/image")
    public UserApp updateUserImage(HttpServletRequest request, @RequestParam("profile-image")MultipartFile image) throws InvalidJwtAuthenticationException, InvalidJwtAuthenticationException {
        return userService.updateUserImage(request,image);
    }
}

