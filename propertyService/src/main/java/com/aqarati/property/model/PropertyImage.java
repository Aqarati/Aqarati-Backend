package com.aqarati.property.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Table(name = "Property_Image")
@Setter
@Getter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PropertyImage implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "img_url")
    private String imgUrl;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "property_id")
    private Property property;

    @Builder.Default
    @Column
    private boolean vr=false;

    @Column(name = "vr_img_url")
    private String vr_url;

}
