package com.Aqarati.property.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "Property")
@Data@Builder
@AllArgsConstructor@NoArgsConstructor
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "price", nullable = false)
    private double price;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @Column(name = "created_time")
    @JsonIgnore
    @Builder.Default
    private Date createdTime=new Date();

}