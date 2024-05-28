package com.aqarati.property;

import com.aqarati.property.repository.PropertyImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import com.aqarati.property.repository.PropertyRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final PropertyRepository propertyRepository;
    private final PropertyImageRepository propertyImageRepository;

    @Override
    public void run(String... args) throws Exception {
//        System.out.println("===============================\n");
//        var p= Property.builder().
//                name("Test").
//                description("haga").
//                price(123123).
//                userId("66115d5fdd56d05f928399f3").
//                build();
//        var pimage= PropertyImage.builder().property(p).imgUrl("qeqe").build();
//        p.getPropertyImages().add(pimage);
//        var x=propertyRepository.save(p);
//
//        System.out.println("===============================\n");
//        System.out.println(x);
//        System.out.println("===============================\n\n");;
//        System.out.println("===============================\n\n");
    }
}
