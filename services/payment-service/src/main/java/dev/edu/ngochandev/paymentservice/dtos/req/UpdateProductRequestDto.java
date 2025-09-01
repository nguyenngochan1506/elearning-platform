package dev.edu.ngochandev.paymentservice.dtos.req;

import dev.edu.ngochandev.paymentservice.commons.enums.CurrencyType;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UpdateProductRequestDto {
    @NotBlank(message = "error.uuid.not-blank")
    private String uuid;
    private String name;
    private String thumbnail;
    private String description;
    private Double price;
    private CurrencyType currency;
    private List<String> itemUuids;
    private Boolean isActive;
}
