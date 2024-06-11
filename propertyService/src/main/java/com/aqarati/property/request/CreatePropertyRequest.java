package com.aqarati.property.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder@Data
@AllArgsConstructor@NoArgsConstructor
public class CreatePropertyRequest {
    private String name;
    private String description;
    private double price;
    private List<String> features = new ArrayList<>();
    private List<String> nearbyLocations = new ArrayList<>();
    private String province;
    private String region;
    private Integer numberOfRooms;
    private Integer numberOfBathrooms;
    private Integer buildingAge;
    private String floor;
    private String propertyArea;
}
