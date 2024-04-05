package com.aqarati.document.repository;

import com.aqarati.document.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertyRepository extends JpaRepository<Property,String> {

}
