package dev.edu.ngochandev.paymentservice.entities;

import dev.edu.ngochandev.paymentservice.commons.BaseEntity;
import dev.edu.ngochandev.paymentservice.commons.enums.ProductItemType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "tbl_product_items")
@Getter
@Setter
@SQLRestriction("is_active = false")
public class ProductItemEntity extends BaseEntity {
    @Column(name = "item_uuid", nullable = false)
    private String itemUuid;

    @Column(name = "item_type", nullable = false)
    private ProductItemType itemType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Override
    public void prePersist() {
        super.prePersist();
        if (this.itemType == null) {
            this.itemType = ProductItemType.COURSE;
        }
    }
}
