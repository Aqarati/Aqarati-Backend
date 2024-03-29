package com.Aqarati.repository;

import com.Aqarati.model.UserApp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserApp,String> {
    Optional<UserApp> findByEmail(String email);
    Optional<UserApp> findByUname(String uname);
}
