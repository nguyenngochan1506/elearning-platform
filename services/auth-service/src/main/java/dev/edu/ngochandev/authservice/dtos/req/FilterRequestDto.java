package dev.edu.ngochandev.authservice.dtos.req;

import dev.edu.ngochandev.authservice.commons.enums.OperatorFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class FilterRequestDto {
    private int page = 1;
    private int size = 10;
    private String search = "";
    private String sort = "id:DESC";
    private List<Filter> filters = List.of();

    @Getter
    @Setter
    public static class Filter{
        private String field;
        private OperatorFilter operator;
        private Object value;
    }
}
