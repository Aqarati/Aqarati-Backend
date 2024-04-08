package com.aqarati.document.repository;

import com.aqarati.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document,Long> {
    List<Document> findAllByUserId(String userID);
}