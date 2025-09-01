package dev.edu.ngochandev.paymentservice.entities;

import dev.edu.ngochandev.paymentservice.commons.enums.ProductItemType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_items")
@Getter
@Setter
@SQLRestriction("is_active = true")
public class ItemEntity extends BaseEntity {
    @Column(name = "item_uuid", nullable = false)
    private String itemUuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "thumbnail", nullable = true)
    private String thumbnail;

    @Column(name = "item_type", nullable = false)
    private ProductItemType itemType;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "items")
    private Set<ProductEntity> products = new HashSet<>();

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tbl_item_categories",
            joinColumns = @JoinColumn(name = "item_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<CategoryEntity> categories = new HashSet<>();

    @Override
    public void prePersist() {
        super.prePersist();
        if (this.itemType == null) {
            this.itemType = ProductItemType.COURSE;
        }
    }
}
