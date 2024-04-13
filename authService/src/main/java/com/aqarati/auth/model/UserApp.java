package com.aqarati.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

@Data
@Builder
@Document("Users")
@AllArgsConstructor@NoArgsConstructor
public class UserApp implements UserDetails {
    @MongoId
    private String id;
    private String email;
    private String uname;
    @JsonIgnore
    private String password;
    private String phoneNumber;
    @Builder.Default
    private List<String> role=new ArrayList<>(Arrays.asList("ROLE_USER"));
    @JsonIgnore
    @Builder.Default
    private Date createdDate=new Date();

    public UserApp(String email, String password,String uname,String phoneNumber) {
        this.email = email;
        this.password = password;
        this.uname = uname;
        this.phoneNumber=phoneNumber;
    }

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        var authority=new ArrayList<GrantedAuthority>();
        for(String r:role){
            authority.add(new SimpleGrantedAuthority(r));
        }
        return authority;
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

