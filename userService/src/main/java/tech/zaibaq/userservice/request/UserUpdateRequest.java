package tech.zaibaq.userservice.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor@NoArgsConstructor@Builder@Data
public class UserUpdateRequest {
    private String firstName;
    private String lastName;
}
