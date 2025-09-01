package dev.edu.ngochandev.paymentservice.dtos.res;

import dev.edu.ngochandev.paymentservice.commons.enums.ProductItemType;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ItemResponse {
    private String itemUuid;
    private ProductItemType itemType;
    private Set<CategoryResponseDto> categories;
}
