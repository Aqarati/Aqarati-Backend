package com.aqarati.document;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import com.aqarati.document.repository.DocumentRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final DocumentRepository documentRepository;

    @Override
    public void run(String... args) throws Exception {

    }
}
