package com.Aqarati.property.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.Aqarati.property.model.Site;

import java.util.List;

public interface SiteRepository extends JpaRepository<Site,Long> {
    List<Site> findAllByUserId(String userID);
}
