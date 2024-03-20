package com.Aqarati;

import com.Aqarati.repository.UserRepository;
import com.Aqarati.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String activeProfile;

    private final AuthService authService;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        //userRepository.deleteAll();
        System.out.println("Active Profile \n \n");
        System.out.println(activeProfile+"\n \n");
    }

}
