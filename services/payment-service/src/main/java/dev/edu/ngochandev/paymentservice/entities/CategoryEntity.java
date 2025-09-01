package dev.edu.ngochandev.paymentservice.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_categories")
@Getter
@Setter
public class CategoryEntity extends BaseEntity{
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "slug", nullable = false)
    private String slug;

    @Column(name = "uuid", nullable = false)
    private String uuid;

    @ManyToMany(mappedBy = "categories")
    private Set<ItemEntity> items = new HashSet<>();
}
