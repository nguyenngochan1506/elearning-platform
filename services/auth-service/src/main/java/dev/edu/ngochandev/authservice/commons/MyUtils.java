package dev.edu.ngochandev.authservice.commons;

import dev.edu.ngochandev.authservice.exceptions.DateFormatException;

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
}
