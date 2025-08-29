package dev.edu.ngochandev.courseservice.features.course.entities;

import dev.edu.ngochandev.courseservice.commons.BaseEntity;
import dev.edu.ngochandev.courseservice.commons.enums.LessonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "tbl_lessons")
@SQLRestriction("is_deleted = false")
public class LessonEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "lesson_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonType lessonType;

    @Column(name = "content", columnDefinition = "TEXT", nullable = true)
    private String content;

    @Column(name = "lesson_order", nullable = false)
    private Integer order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chapter_id", nullable = false)
    private ChapterEntity chapter;

    @Override
    public void prePersist() {
        super.prePersist();
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
        }
        if(order == null) {
            Integer maxOrder = chapter.getLessons().stream()
                    .map(LessonEntity::getOrder)
                    .max(Integer::compareTo)
                    .orElse(0);
            order = maxOrder + 1;
        }
    }
}
