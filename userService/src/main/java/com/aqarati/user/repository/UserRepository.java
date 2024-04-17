package com.aqarati.user.repository;

import com.aqarati.user.model.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserApp,String> {
    Optional<UserApp> findByEmail(String email);
}
