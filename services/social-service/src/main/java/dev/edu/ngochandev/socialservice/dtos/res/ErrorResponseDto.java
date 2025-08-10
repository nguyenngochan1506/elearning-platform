package dev.edu.ngochandev.socialservice.dtos.res;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponseDto {
    private int status;
    private String error;
    private String message;
    private Date timestamp;
    private String path;
    private Map<String, String> errors;

    public ErrorResponseDto(HttpStatus status, String message, WebRequest webRequest) {
        this.status = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
        this.timestamp = new Date();
        this.path = webRequest != null ? webRequest.getDescription(false).replace("uri=", "") : null;
    }

    public void addValidationError(String field, String message) {
        if (errors == null) {
            errors = new HashMap<>();
        }
        errors.put(field, message);
    }
}
