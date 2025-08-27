package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.RoleEntity;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query("SELECT r FROM RoleEntity r WHERE r.organization.id = :orgId AND (LOWER(r.name) LIKE LOWER(:search) OR LOWER(r.description) LIKE LOWER(:search))")
    Page<RoleEntity> findByOrganizationAndSearch(@Param("orgId") Long orgId, @Param("search") String search, Pageable pageable);

    @Query("SELECT r FROM RoleEntity r WHERE r.organization.id IS NULL AND (LOWER(r.name) LIKE LOWER(:search) OR LOWER(r.description) LIKE LOWER(:search))")
    Page<RoleEntity> findGlobalRolesBySearch(@Param("search") String search, Pageable pageable);

    Optional<RoleEntity> findByName(String name);

    @Query("SELECT r FROM RoleEntity r WHERE r.name = :name AND r.organization.id IS NULL")
    Optional<RoleEntity> findByNameAndOrganizationIsNull(@Param("name") String name);

    @Query("SELECT r FROM RoleEntity r WHERE r.id IN :ids AND (r.organization.id = :orgId OR r.organization.id IS NULL)")
    List<RoleEntity> findByIdsInOrganizationOrGlobal(@Param("ids") List<Long> ids, @Param("orgId") Long orgId);

    @Query("SELECT r FROM RoleEntity r LEFT JOIN FETCH r.rolePermissions WHERE r.name = :name")
    Optional<RoleEntity> findByNameWithPermissions(@Param("name") String name);

}
