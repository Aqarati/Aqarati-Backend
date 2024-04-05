package com.aqarati.document.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;


@Entity
@Table(name = "Document")
@Data@Builder
@AllArgsConstructor@NoArgsConstructor
public class Document {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id",nullable = false)
    private String userId;

    @Column(name = "property_id",nullable = false)
    private String propertyId;

    @Column(name = "img_url")
    private String imgUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    @Column(name = "status")
    private DocumentStatus status=DocumentStatus.PENDING;

    @Column(name = "created_time")
    @JsonIgnore
    @Builder.Default
    private Date createdTime=new Date();

}