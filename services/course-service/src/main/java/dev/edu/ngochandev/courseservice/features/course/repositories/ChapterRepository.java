package dev.edu.ngochandev.courseservice.features.course.repositories;

import dev.edu.ngochandev.courseservice.features.course.entities.ChapterEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChapterRepository extends JpaRepository<ChapterEntity, Long> {
    Optional<ChapterEntity> findByUuid(String uuid);
    @Modifying
    @Query("UPDATE ChapterEntity c SET c.isDeleted = true WHERE c.course.id IN :courseIds")
    void softDeleteByCourseIds(@Param("courseIds") List<Long> courseIds);
}
