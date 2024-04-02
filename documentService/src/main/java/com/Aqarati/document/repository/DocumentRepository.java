package com.Aqarati.document.repository;

import com.Aqarati.document.model.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document,String> {

}
