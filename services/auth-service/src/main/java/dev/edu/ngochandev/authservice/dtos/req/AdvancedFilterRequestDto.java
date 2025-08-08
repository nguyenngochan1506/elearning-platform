package dev.edu.ngochandev.authservice.dtos.req;

import dev.edu.ngochandev.authservice.commons.enums.OperatorFilter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class AdvancedFilterRequestDto extends SimpleFilterRequestDto{

	private List<FilterData> filters = List.of();
	@Getter
	@Setter
	public static class FilterData {
		private String field;
		private OperatorFilter operator;
		private Object value;
	}

}
