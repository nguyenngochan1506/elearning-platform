package dev.edu.ngochandev.courseservice.features.course.repositories;

import dev.edu.ngochandev.courseservice.commons.enums.AuthorshipType;
import dev.edu.ngochandev.courseservice.features.course.entities.ResourceAuthorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResourceAuthorRepository extends JpaRepository<ResourceAuthorEntity, Long> {
    @Query("SELECT CASE WHEN COUNT(ra) > 0 THEN true ELSE false END " +
            "FROM ResourceAuthorEntity ra " +
            "WHERE ra.resourceUuid = :resourceUuid " +
            "AND ra.userId = :userId " +
            "AND ra.authorshipType IN :author")
    boolean existsByResourceUuidAndUserIdAndAuthorshipRoleIn(String resourceUuid, Long userId, List<AuthorshipType> author);
}
