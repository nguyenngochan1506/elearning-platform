package dev.edu.ngochandev.courseservice.features.course.entities;

import dev.edu.ngochandev.courseservice.commons.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_lessons")
public class LessonEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "lesson_type", nullable = false)
    private String lessonType;

    @Column(name = "content", columnDefinition = "JSON", nullable = true)
    private String content;

    @Column(name = "lesson_order", nullable = false)
    private Integer order;

    @Override
    public void prePersist() {
        super.prePersist();
        if (uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
        }
        if(order == null) {
            order = 0;
        }
    }
}
