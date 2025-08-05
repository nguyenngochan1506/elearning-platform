package dev.edu.ngochandev.authservice.entities;

import dev.edu.ngochandev.authservice.commons.enums.HttpMethod;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name = "tbl_permissions")
@Getter
@Setter
@NoArgsConstructor
public class PermissionEntity extends BaseEntity {
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "method", nullable = false)
    @Enumerated(EnumType.STRING)
    private HttpMethod method;
    @Column(name = "api_path", nullable = false)
    private String apiPath;
    @Column(name = "module", nullable = false)
    private String module;

    @OneToMany(mappedBy = "permission", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<RolePermissionEntity> rolePermissions;

    public PermissionEntity(Long id){
        this.setId(id);
    }
}
