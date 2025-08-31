package dev.edu.ngochandev.paymentservice.entities;


import dev.edu.ngochandev.paymentservice.commons.enums.CurrencyType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLRestriction;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tbl_products")
@Getter
@Setter
@SQLRestriction("is_active = true")
public class ProductEntity extends BaseEntity {
    @Column(name = "uuid", nullable = false, unique = true)
    private String uuid;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "thumbnail", nullable = true)
    private String thumbnail;

    @Column(name = "description", nullable = true, columnDefinition = "TEXT")
    private String description;

    @Column(name = "price", nullable = false)
    private Long price;

    @Column(name = "currency", nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private CurrencyType currency;

    @Column(name = "organization_uuid", nullable = false)
    private String organizationUuid;

    @Column(name = "created_by", nullable = false)
    private Long createdBy;

    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "products")
    private Set<ItemEntity> items = new HashSet<>();

    @Override
    public void prePersist(){
        super.prePersist();
        if(this.price == null) {
            this.price = 0L;
        }
        if (this.currency == null) {
            this.currency = CurrencyType.VND;
        }
    }
}
