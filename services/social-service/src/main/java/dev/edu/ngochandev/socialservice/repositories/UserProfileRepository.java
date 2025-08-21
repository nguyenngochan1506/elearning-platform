package dev.edu.ngochandev.socialservice.repositories;

import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends MongoRepository<UserProfileEntity, String> {
    boolean existsByUserId(Long userId);
    UserProfileEntity findByUserId(Long userId);
}
