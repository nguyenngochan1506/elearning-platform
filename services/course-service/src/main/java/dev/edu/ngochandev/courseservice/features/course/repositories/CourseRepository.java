package dev.edu.ngochandev.courseservice.features.course.repositories;

import dev.edu.ngochandev.courseservice.features.course.entities.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, Long> {
    boolean existsBySlug(String slug);
}
