package com.Aqarati.property;

import com.Aqarati.property.model.Property;
import jakarta.persistence.Column;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import com.Aqarati.property.repository.PropertyRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final PropertyRepository propertyRepository;

    @Override
    public void run(String... args) throws Exception {
        var p= Property.builder().
                name("Sweet home ").
                description("big 4 room asdjaskldas ").
                imgUrl("https://images.pexels.com/photos/106399/pexels-photo-106399.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500").
                price(123123).build();
        System.out.println(p);
        System.out.println("*****************************************************************");
        propertyRepository.save(p);
    }
}
