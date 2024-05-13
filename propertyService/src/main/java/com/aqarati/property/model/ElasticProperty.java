package com.aqarati.property.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Document(indexName = "property" ,createIndex = true)
@Setter@Getter@Builder
@AllArgsConstructor@NoArgsConstructor
public class ElasticProperty implements Serializable {
    @Id
    private Long id;

    @Field(name = "name")
    private String name;

    @Field(name = "description")
    private String description;

    @Field(name = "price")
    private Double price;

    @Field(name = "user_id")
    private String userId;

    @Builder.Default
    @Field
    private List<PropertyImage> propertyImages = new ArrayList<>();

    @JsonIgnore
    @Builder.Default
    private Date createdTime=new Date();

}