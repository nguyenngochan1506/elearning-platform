package dev.edu.ngochandev.courseservice.features.course.entities;

import dev.edu.ngochandev.courseservice.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tbl_chapters")
@SQLRestriction("is_deleted = false")
public class ChapterEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "chapter_order", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<LessonEntity> lessons = new HashSet<>();

    @Override
    public void prePersist() {
        super.prePersist();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        if(order == null) {
            Integer maxOrder = this.course.getChapters().stream().map(ChapterEntity::getOrder).max(Integer::compareTo).orElse(0);
            order = maxOrder + 1;
        }
    }
}
