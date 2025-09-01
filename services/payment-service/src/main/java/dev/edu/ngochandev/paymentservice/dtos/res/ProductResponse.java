package dev.edu.ngochandev.paymentservice.dtos.res;

import dev.edu.ngochandev.paymentservice.commons.enums.CurrencyType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProductResponse {
    private String uuid;
    private String name;
    private String description;
    private String thumbnail;
    private Long price;
    private CurrencyType currency;
    private String organizationUuid;
    private Set<ItemResponse> items;
}
