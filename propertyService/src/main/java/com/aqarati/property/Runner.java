package com.aqarati.property;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import com.aqarati.property.repository.PropertyRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final PropertyRepository propertyRepository;

    @Override
    public void run(String... args) throws Exception {

    }
}
