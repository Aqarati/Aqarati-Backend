package com.aqarati.property.repository;

import com.aqarati.property.model.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PropertyRepository extends JpaRepository<Property,Long> {
    List<Property> findAllByUserId(String userId);
    @Query("SELECT p FROM Property p WHERE LOWER(p.name) LIKE LOWER(:keyword) OR LOWER(p.description) LIKE LOWER(:keyword)")
    List<Property> searchByKeyword(@Param("keyword") String keyword);
}
