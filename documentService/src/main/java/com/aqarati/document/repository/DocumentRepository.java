package com.aqarati.document.repository;

import com.aqarati.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,String> {

}
