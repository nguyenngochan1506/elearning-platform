package dev.edu.ngochandev.paymentservice.dtos.req;

import dev.edu.ngochandev.paymentservice.commons.enums.CurrencyType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateProductRequestDto {
    @NotBlank(message = "error.name.not-blank")
    private String name;

    private String thumbnail;

    private String description;

    @NotNull(message = "error.price.not-null")
    private Double price;

    private CurrencyType currency;

    private List<String> itemUuids;
}
