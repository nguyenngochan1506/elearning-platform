package dev.edu.ngochandev.authservice.dtos.req;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class SimpleFilterRequestDto {
    private int page = 1;
    private int size = 10;
    private String search = "";

    @Pattern(regexp = "^[a-zA-Z_]\\w*:(ASC|DESC|asc|desc)$", message = "error.sort.invalid")
    private String sort = "id:DESC";
}
