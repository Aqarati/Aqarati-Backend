package com.aqarati.userservice.repository;

import com.aqarati.userservice.model.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserApp,String> {
    Optional<UserApp> findByEmail(String email);
}
