package com.Aqarati.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data@Builder
public class AuthRequest {
    @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,5}",
            flags = Pattern.Flag.CASE_INSENSITIVE,message = "Email Invalid")
    @NotNull(message = "email is required")
    private String email;
    @NotNull(message = "password is required")
    @Size(min = 4, message = "Password must be at least 4 characters long")
    private String password;
    @NotNull(message = "username is required")
    @Size(min = 4, message = "username must be at least 4 characters long")
    private String username;
    @Pattern(regexp = "^07\\d{8}$", message = "Phone number must start with 07 and be followed by 8 digits")
    @NotNull(message = "phone-number is required")
    private String phoneNumber;
}
