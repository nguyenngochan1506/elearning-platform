package dev.edu.ngochandev.paymentservice.dtos.res;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategoryResponseDto {
    private String uuid;
    private String name;
    private String slug;
}
