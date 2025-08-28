package dev.edu.ngochandev.courseservice.features.course.entities;

import dev.edu.ngochandev.courseservice.commons.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "tbl_chapters")
public class ChapterEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "chapter_order", nullable = false)
    private Integer order;

    @Override
    public void prePersist() {
        super.prePersist();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
        if(order == null) {
            order = 0;
        }
    }
}
