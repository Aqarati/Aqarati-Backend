package com.aqarati;

import com.aqarati.repository.UserRepository;
import com.aqarati.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;


@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {

    @Value("${spring.profiles.active}")
    private String activeProfile;
    Logger logger = Logger.getLogger(getClass().getName());

    private final AuthService authService;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        logger.info("Active Profile");
        logger.info(activeProfile);
    }

}
