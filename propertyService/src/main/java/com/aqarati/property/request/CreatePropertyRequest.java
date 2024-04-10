package com.aqarati.property.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder@Data
@AllArgsConstructor@NoArgsConstructor
public class CreatePropertyRequest {
    private String name;
    private String description;
    private double price;
}
