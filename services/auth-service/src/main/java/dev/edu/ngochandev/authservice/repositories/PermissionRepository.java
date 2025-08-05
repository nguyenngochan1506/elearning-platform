package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface PermissionRepository extends JpaRepository<PermissionEntity, Long> {
    @Query("SELECT p " +
            "FROM PermissionEntity p " +
            "WHERE LOWER(p.name) LIKE LOWER(:search) OR" +
            " LOWER(p.apiPath) LIKE LOWER(:search) OR" +
            " LOWER(p.method) LIKE LOWER(:search) OR" +
            " LOWER(p.module) LIKE LOWER(:search)"
    )
    Page<PermissionEntity> findBySearch(@Param("search") String search, Pageable pageable);
}
