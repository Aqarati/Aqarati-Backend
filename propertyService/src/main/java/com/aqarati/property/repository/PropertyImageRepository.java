package com.aqarati.property.repository;

import com.aqarati.property.model.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface PropertyImageRepository extends CrudRepository<PropertyImage,Long> {
}
