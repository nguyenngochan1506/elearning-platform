package dev.edu.ngochandev.courseservice.features.course.entities;

import dev.edu.ngochandev.courseservice.commons.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "tbl_categories")
@SQLRestriction("is_deleted = false")
public class CategoryEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", columnDefinition = "TEXT", nullable = true)
    private String description;

    @Column(name = "slug", nullable = false, unique = true)
    private String slug;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id", nullable = true)
    private CategoryEntity parent;

    @OneToMany(mappedBy = "parent", fetch = FetchType.LAZY)
    private Set<CategoryEntity> children = new HashSet<>();

    @ManyToMany(mappedBy = "categories", fetch = FetchType.LAZY)
    private Set<CourseEntity> courses = new HashSet<>();

    @Override
    public void prePersist() {
        super.prePersist();
        if(uuid == null) {
            uuid = java.util.UUID.randomUUID().toString();
        }
    }
}
