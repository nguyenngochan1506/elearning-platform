package dev.edu.ngochandev.authservice.repositories;

import dev.edu.ngochandev.authservice.entities.UserEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long>, JpaSpecificationExecutor<UserEntity> {
	boolean existsByUsername(String username);

	boolean existsByEmail(String email);

	@Query("SELECT u FROM UserEntity u WHERE u.username = :identifier OR u.email = :identifier")
	Optional<UserEntity> findByUsernameOrEmail(String identifier);

	@Modifying
	@Query("UPDATE UserEntity u SET u.isDeleted = true, u.updatedAt = CURRENT_TIMESTAMP WHERE u.id IN :ids AND u.isDeleted = false")
	void softDeleteAllByIds(@Param("ids") Set<Long> ids);
}
