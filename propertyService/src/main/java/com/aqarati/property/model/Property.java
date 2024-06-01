package com.aqarati.property.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.Reference;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.*;


//@Entity
//@Table(name = "property")
@Setter@Getter@Builder
@AllArgsConstructor@NoArgsConstructor
@RedisHash
public class Property implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private Double price;

    @Builder.Default
    @Column(name = "verified", nullable = false)
    private boolean verified = false;


    @Column(name = "user_id",nullable = false)
    private String userId;

    @Builder.Default
//    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @Reference
    private List<PropertyImage> propertyImages = new ArrayList<>();

    @Column(name = "created_time")
    @JsonIgnore
    @Builder.Default
    private Date createdTime=new Date();

}