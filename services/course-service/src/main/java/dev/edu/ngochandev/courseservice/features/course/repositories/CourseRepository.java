package dev.edu.ngochandev.courseservice.features.course.repositories;

import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    boolean existsBySlug(String slug);

    Optional<CourseEntity> findByUuid(String uuid);

    @Query("SELECT c FROM CourseEntity c WHERE " +
            "LOWER(c.name) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :search, '%')) OR " +
            "LOWER(c.slug) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<CourseEntity> findBySearch(String search, Pageable pageable);
}
