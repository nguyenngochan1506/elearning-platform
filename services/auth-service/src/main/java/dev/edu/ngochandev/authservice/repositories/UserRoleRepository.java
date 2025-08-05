package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.UserRoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
}
