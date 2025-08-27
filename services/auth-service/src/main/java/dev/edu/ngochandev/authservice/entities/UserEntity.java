package dev.edu.ngochandev.authservice.entities;

import dev.edu.ngochandev.authservice.commons.enums.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tbl_users")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SQLRestriction("is_deleted = false")
public class UserEntity extends BaseEntity {
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "status", nullable = true)
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Column(name = "last_login_at", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime lastLoginAt;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
    private Set<UserOrganizationRoleEntity> userOrganizationRoles = new HashSet<>();

}
