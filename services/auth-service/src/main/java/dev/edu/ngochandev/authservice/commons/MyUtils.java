package dev.edu.ngochandev.authservice.commons;

import dev.edu.ngochandev.authservice.dtos.req.SimpleFilterRequestDto;
import dev.edu.ngochandev.authservice.exceptions.DateFormatException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class MyUtils {
	public static LocalDateTime parseFlexibleDate(String date) {
		try{
			return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
		} catch (DateTimeParseException e) {
			throw new DateFormatException("error.date.invalid");
		}
	}

	public static <T extends SimpleFilterRequestDto> Pageable createPageable( T filter) {
		//sort
		String[] sort = filter.getSort().split(":");
		String sortField = sort[0];
		Sort.Direction direction = sort[1].equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
		//pageable
		int page = filter.getPage() > 0 ? filter.getPage() - 1 : 0;
		int size = filter.getSize();

		return PageRequest.of(page, size, Sort.by(direction, sortField));
	}
}
