package com.aqarati.user;


import com.aqarati.user.publisher.Publisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    private final Publisher publisher;

    @Override
    public void run(String... args) throws Exception {
//        publisher.publishMessage("1");
    }

}
