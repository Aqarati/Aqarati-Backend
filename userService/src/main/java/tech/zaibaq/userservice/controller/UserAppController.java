package tech.zaibaq.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
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

}

