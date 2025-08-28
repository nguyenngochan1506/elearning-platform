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
@Table(name = "tbl_courses")
public class CourseEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "thumbnail",columnDefinition = "TEXT", nullable = true)
    private String thumbnail;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "oganization_uuid", nullable = true)
    private String organizationUuid;


    @Override
    public void prePersist() {
        super.prePersist();
        if (isPublic == null) {
            isPublic = false;
        }
        if(uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }
}
