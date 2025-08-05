package dev.edu.ngochandev.authservice.entities;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLRestriction;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "tbl_roles")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SQLRestriction("is_deleted = false")
public class RoleEntity extends BaseEntity{
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = true)
    private String description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<UserRoleEntity> userRoles;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<RolePermissionEntity> rolePermissions;

    public Set<PermissionEntity> getPermissions() {
        if (rolePermissions == null) {
            return null;
        }
        return rolePermissions.stream()
                .map(RolePermissionEntity::getPermission)
                .collect(Collectors.toSet());
    }
}
