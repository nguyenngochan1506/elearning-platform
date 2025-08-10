package dev.edu.ngochandev.socialservice.repositories;

import dev.edu.ngochandev.socialservice.entities.UserProfileEntity;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserProfileRepository extends Neo4jRepository<UserProfileEntity, String> {
    boolean existsByUserId(Long userId);
}
