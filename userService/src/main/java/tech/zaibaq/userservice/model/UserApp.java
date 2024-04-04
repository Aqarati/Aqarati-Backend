package tech.zaibaq.userservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Date;

@Data
@Builder
@Document("Users")
@AllArgsConstructor
public class UserApp  {
    @MongoId
    private String id;
    private String email;
    private String uname;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String imageUrl;
    @JsonIgnore
    private Date createdDate;

    public UserApp() {
        this.createdDate = new Date();
    }

    public UserApp(String email, String password) {
        this();
        this.email = email;
        this.password = password;
    }

    public UserApp(String email, String password, String firstName, String lastName) {
        this(email, password);
        this.firstName = firstName;
        this.lastName = lastName;
    }
}

