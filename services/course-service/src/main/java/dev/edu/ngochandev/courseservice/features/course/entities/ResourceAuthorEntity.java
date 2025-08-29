package dev.edu.ngochandev.courseservice.features.course.entities;

import dev.edu.ngochandev.courseservice.commons.BaseEntity;
import dev.edu.ngochandev.courseservice.commons.enums.AuthorshipType;
import dev.edu.ngochandev.courseservice.commons.enums.ResourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Setter
@Entity
@Table(name = "tbl_resource_authors")
@SQLRestriction("is_deleted = false")
public class ResourceAuthorEntity extends BaseEntity {

    @Column(name = "resource_uuid", nullable = false)
    private String resourceUuid;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "authorship_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorshipType authorshipType;

    @Column(name = "resource_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;

    @Override
    public void prePersist() {
        super.prePersist();
        if (authorshipType == null) {
            authorshipType = AuthorshipType.AUTHOR;
        }
        if (resourceType == null) {
            resourceType = ResourceType.COURSE;
        }
    }
}
