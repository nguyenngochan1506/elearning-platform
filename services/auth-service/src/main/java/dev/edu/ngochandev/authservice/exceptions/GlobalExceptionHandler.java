package dev.edu.ngochandev.authservice.exceptions;

import dev.edu.ngochandev.authservice.common.Translator;
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
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        return new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR,message, req);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponseDto handleResourceNotFound(Exception ex, WebRequest req) {
        return new ErrorResponseDto(HttpStatus.NOT_FOUND, Translator.translate(ex.getMessage()), req);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest req) {
        ErrorResponseDto res = new ErrorResponseDto(HttpStatus.BAD_REQUEST, Translator.translate("error.body.invalid"), req);
        ex.getFieldErrors().forEach(error ->{
            res.addValidationError(error.getField(), Translator.translate(error.getDefaultMessage()));
        });

        return res;
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponseDto handleUnauthorizedException(UnauthorizedException ex, WebRequest req) {
        return  new ErrorResponseDto(HttpStatus.UNAUTHORIZED,Translator.translate(ex.getMessage()), req);
    }

    @ExceptionHandler(DuplicateResourceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponseDto handleDuplicateResourceException(DuplicateResourceException ex, WebRequest req) {
        return new ErrorResponseDto(HttpStatus.CONFLICT,Translator.translate(ex.getMessage()), req);
    }
}
