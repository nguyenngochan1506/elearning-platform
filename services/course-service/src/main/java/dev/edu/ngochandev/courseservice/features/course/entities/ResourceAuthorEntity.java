package dev.edu.ngochandev.courseservice.features.course.entities;

import dev.edu.ngochandev.courseservice.commons.BaseEntity;
import dev.edu.ngochandev.courseservice.commons.enums.AuthorshipType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "tbl_resource_authors")
public class ResourceAuthorEntity extends BaseEntity {

    @Column(name = "resource_uuid", nullable = false)
    private String uuid;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "authorship_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private AuthorshipType authorshipType;

    @Override
    public void prePersist() {
        super.prePersist();
        if (authorshipType == null) {
            authorshipType = AuthorshipType.AUTHOR;
        }
    }
}
