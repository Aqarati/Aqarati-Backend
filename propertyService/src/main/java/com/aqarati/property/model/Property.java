package com.aqarati.property.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.*;

@Entity
@Table(name = "property")
@Setter@Getter@Builder
@AllArgsConstructor@NoArgsConstructor
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

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Builder.Default
    @OneToMany(mappedBy = "property", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<PropertyImage> propertyImages = new ArrayList<>();

    @Column(name = "created_time")
    @JsonIgnore
    @Builder.Default
    private Date createdTime = new Date();

    @Column(name = "property_status")
    @Builder.Default
    private PropertyStatus propertyStatus = PropertyStatus.AVAILABLE;

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "selected_features")
    private List<String> features = new ArrayList<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @Column(name = "selected_nearby_locations")
    private List<String> nearbyLocations = new ArrayList<>();

    @Column(name = "province")
    private String province;

    @Column(name = "region")
    private String region;

    @Column(name = "number_of_rooms")
    private Integer numberOfRooms;

    @Column(name = "number_of_bathrooms")
    private Integer numberOfBathrooms;

    @Column(name = "building_age")
    private Integer buildingAge;

    @Column(name = "floor")
    private String floor;

    @Column(name = "property_area")
    private String propertyArea;
}
