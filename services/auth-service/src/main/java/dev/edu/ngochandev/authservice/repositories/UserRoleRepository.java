package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.UserRoleEntity;
import java.util.Set;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserRoleRepository extends JpaRepository<UserRoleEntity, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE UserRoleEntity ur SET ur.isDeleted = true WHERE ur.id IN :ids AND ur.isDeleted = false")
    void softDeleteAllByIds(@Param("ids") Set<Long> ids);
}
