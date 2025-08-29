package dev.edu.ngochandev.courseservice.features.course.repositories;

import dev.edu.ngochandev.courseservice.features.course.entities.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, Long> {
    boolean existsBySlug(String slug);

    @Query("SELECT c FROM CategoryEntity c WHERE (LOWER(c.name) LIKE LOWER(:search) OR LOWER(c.description) LIKE LOWER(:search) OR LOWER(c.slug) LIKE LOWER(:search))")
    Page<CategoryEntity> findBySearch(String search, Pageable pageable);

    Optional<CategoryEntity> findByUuid(String uuid);

    @Query("SELECT c FROM CategoryEntity c WHERE c.uuid IN :uuids")
    List<CategoryEntity> findAllByUuid(List<String> uuids);
}
