package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.RolePermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermissionEntity, Long> {
}
