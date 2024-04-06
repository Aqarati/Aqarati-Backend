package com.aqarati;

import com.aqarati.model.UserApp;
import com.aqarati.repository.UserRepository;
import com.aqarati.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
//       var user=createAdminUser();
//        userRepository.save(user);
//        logger.info("crated user "+user.toString());
        logger.info("Active Profile");
        logger.info(activeProfile);


    }
    private UserApp createAdminUser(String email,String username,String password,String phoneNumber){
        var adminUser= UserApp.builder().
                email(email)
                .uname(username)
                .password(passwordEncoder.encode(password))
                .phoneNumber(phoneNumber)
                .build();
        adminUser.getRole().add("ROLE_ADMIN");
        return adminUser;
    }
}
