package com.Aqarati;


import com.Aqarati.repository.UserRepository;
import com.Aqarati.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Runner implements CommandLineRunner {
    private final AuthService authService;
    private final UserRepository userRepository;
    @Override
    public void run(String... args) throws Exception {
        //userRepository.deleteAll();
    }

}
