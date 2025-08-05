package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.RoleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query("SELECT r FROM RoleEntity r " +
            "WHERE r.name LIKE LOWER(:search) OR " +
            "r.description LIKE LOWER(:search)")
    Page<RoleEntity> findBySearch(@Param("search") String search, Pageable pageable);
}
