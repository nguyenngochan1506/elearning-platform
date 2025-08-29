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
@Table(name = "tbl_courses")
@SQLRestriction("is_deleted = false")
public class CourseEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "thumbnail",columnDefinition = "TEXT", nullable = true)
    private String thumbnail;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @Column(name = "organization_uuid", nullable = true)
    private String organizationUuid;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "tbl_course_categories",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ChapterEntity> chapters = new HashSet<>();

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
