package com.Aqarati.property.repository;

import com.Aqarati.property.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property,String> {

}
