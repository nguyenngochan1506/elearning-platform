package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.commons.enums.HttpMethod;
import dev.edu.ngochandev.authservice.entities.PermissionEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


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
    long countByIdIn(Collection<Long> id);

    @Query("SELECT DISTINCT p " +
            "FROM PermissionEntity p " +
            "JOIN p.rolePermissions rp " +
            "JOIN rp.role r " +
            "WHERE r.name IN :roleNames")
    List<PermissionEntity> findAllByRoleNames(@Param("roleNames") Collection<String> roleNames);

    Optional<PermissionEntity> findByApiPathAndMethod(String apiPath, HttpMethod method);
}
