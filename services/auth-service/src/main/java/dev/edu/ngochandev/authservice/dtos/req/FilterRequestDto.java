package dev.edu.ngochandev.authservice.dtos.req;

import dev.edu.ngochandev.authservice.commons.enums.OperatorFilter;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FilterRequestDto {
    private int page = 1;
    private int size = 10;
    private String search = "";
    @Pattern(regexp = "^[a-zA-Z_][a-zA-Z0-9_]*:(ASC|DESC|asc|desc)$", message = "error.sort.invalid")
    private String sort = "id:DESC";
    private List<FilterData> filters = List.of();

    @Getter
    @Setter
    public static class FilterData {
        private String field;
        private OperatorFilter operator;
        private Object value;
    }

}