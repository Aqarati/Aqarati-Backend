package com.aqarati.auth.repository;

import com.aqarati.auth.model.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserApp,String> {
    Optional<UserApp> findByEmail(String email);
    Optional<UserApp> findByUname(String uname);
    Optional<UserApp> findByPhoneNumber(String phoneNumber);
}
