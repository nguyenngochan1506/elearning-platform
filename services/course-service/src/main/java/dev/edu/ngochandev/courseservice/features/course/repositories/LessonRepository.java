package dev.edu.ngochandev.courseservice.features.course.repositories;

import dev.edu.ngochandev.courseservice.features.course.entities.LessonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LessonRepository extends JpaRepository<LessonEntity, Long> {
    Optional<LessonEntity> findByUuid(String uuid);
    @Modifying
    @Query("UPDATE LessonEntity l SET l.isDeleted = true WHERE l.chapter.id IN (SELECT c.id FROM ChapterEntity c WHERE c.course.id IN :courseIds)")
    void softDeleteByCourseIds(@Param("courseIds") List<Long> courseIds);
}
