package dev.edu.ngochandev.authservice.dtos.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResponseDto implements Serializable {
    private int status;
    private String error;
    private String message;
    private Date timestamp;
    private String path;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Map<String, String> errors;

    public void addValidationError(String field, String message) {
        if (errors == null) {
            errors = new java.util.HashMap<>();
        }
        errors.put(field, message);
    }
}
