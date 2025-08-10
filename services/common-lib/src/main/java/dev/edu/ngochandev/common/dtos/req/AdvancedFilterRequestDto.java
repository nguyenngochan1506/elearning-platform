package dev.edu.ngochandev.common.dtos.req;

import java.util.List;

import dev.edu.ngochandev.common.enums.OperatorFilter;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AdvancedFilterRequestDto extends SimpleFilterRequestDto  {

    private List<FilterData> filters = List.of();

    @Getter
    @Setter
    public static class FilterData {
        private String field;
        private OperatorFilter operator;
        private Object value;
    }
}
