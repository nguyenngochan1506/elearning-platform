package dev.edu.ngochandev.authservice.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "tbl_organizations")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationEntity extends BaseEntity {

    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "name", nullable = false, length = 255)
    private String name;

    @Override
    public void prePersist() {
        super.prePersist();
        if (uuid == null) {
            uuid = UUID.randomUUID().toString();
        }
    }
}
