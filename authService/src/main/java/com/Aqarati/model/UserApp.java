package com.Aqarati.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;

@Data
@Builder
@Document("Users")
@AllArgsConstructor
public class UserApp implements UserDetails {
    @MongoId
    private String id;
    private String email;
    @JsonIgnore
    private String password;
    private String firstName;
    private String lastName;
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

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return AuthorityUtils.NO_AUTHORITIES;
    }

    @Override
    @JsonIgnore
    public String getPassword() {
        return password;
    }

    @Override
    @JsonIgnore
    public String getUsername() {
        return email;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @JsonIgnore
    public boolean isEnabled() {
        return true;
    }
}

