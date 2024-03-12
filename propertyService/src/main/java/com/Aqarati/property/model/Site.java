package com.Aqarati.property.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "site")
public class Site {
    @Id
    private Long id;
    private String name;
    private String userId;
}
