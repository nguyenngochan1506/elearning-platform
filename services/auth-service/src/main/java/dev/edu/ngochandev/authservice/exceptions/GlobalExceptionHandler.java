package dev.edu.ngochandev.authservice.exceptions;

import dev.edu.ngochandev.authservice.dtos.res.ErrorResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleGlobalException(Exception ex, WebRequest req) {
        ErrorResponseDto res = new ErrorResponseDto();
        res.setPath(req.getDescription(false).replace("uri=", ""));
        res.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        res.setTimestamp(new Date());
        res.setError(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        res.setMessage(ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred");

        return res;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest req) {
        ErrorResponseDto res = new ErrorResponseDto();
        res.setPath(req.getDescription(false).replace("uri=", ""));
        res.setStatus(HttpStatus.BAD_REQUEST.value());
        res.setTimestamp(new Date());
        res.setError(HttpStatus.BAD_REQUEST.getReasonPhrase());
        res.setMessage("Validation failed");
        ex.getFieldErrors().forEach(error ->{
            res.addValidationError(error.getField(), error.getDefaultMessage());
        });

        return res;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleUnauthorizedException(UnauthorizedException ex, WebRequest req) {
        ErrorResponseDto res = new ErrorResponseDto();
        res.setPath(req.getDescription(false).replace("uri=", ""));
        res.setStatus(HttpStatus.UNAUTHORIZED.value());
        res.setTimestamp(new Date());
        res.setMessage(ex.getMessage());
        res.setError("Unauthorized access");

        return res;
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleDuplicateResourceException(DuplicateResourceException ex, WebRequest req) {
        ErrorResponseDto res = new ErrorResponseDto();
        res.setPath(req.getDescription(false).replace("uri=", ""));
        res.setStatus(HttpStatus.CONFLICT.value());
        res.setTimestamp(new Date());
        res.setMessage(ex.getMessage());
        res.setError("Duplicate resource");

        return res;
    }
}
