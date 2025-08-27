package dev.edu.ngochandev.authservice.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tbl_organizations")
@Getter
@Setter
public class OrganizationEntity extends BaseEntity {

    @Column(name = "uuid", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.UUID)
    private String uuid;

    @Column(name = "slug", nullable = false, unique = true, length = 100)
    private String slug;

    @Column(name = "name", nullable = false, length = 255)
    private String name;
}
