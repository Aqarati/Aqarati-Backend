package tech.zaibaq.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import tech.zaibaq.userservice.model.UserApp;

import java.util.Optional;

public interface UserRepository extends MongoRepository<UserApp,String> {
    Optional<UserApp> findByEmail(String email);
}
