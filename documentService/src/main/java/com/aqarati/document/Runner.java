package com.aqarati.document;

import com.aqarati.document.repository.PropertyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import com.aqarati.document.repository.DocumentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final DocumentRepository documentRepository;
    private final PropertyRepository propertyRepository;

    @Override
    public void run(String... args) throws Exception {
        System.out.println(propertyRepository.findAll());
    }
}
