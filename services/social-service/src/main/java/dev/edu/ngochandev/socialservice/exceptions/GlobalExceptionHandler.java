package dev.edu.ngochandev.socialservice.exceptions;

import dev.edu.ngochandev.common.dtos.res.ErrorResponseDto;
import dev.edu.ngochandev.common.i18n.Translator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final Translator translator;

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponseDto handleGlobalException(Exception ex, WebRequest req) {
        String message = ex.getMessage() != null ? ex.getMessage() : "An unexpected error occurred";
        return new ErrorResponseDto(HttpStatus.INTERNAL_SERVER_ERROR, message, req);
    }
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponseDto handleValidationExceptions(MethodArgumentNotValidException ex, WebRequest req) {
        ErrorResponseDto res =
                new ErrorResponseDto(HttpStatus.BAD_REQUEST, translator.translate("error.body.invalid"), req);
        if (ex.getBindingResult().hasErrors()) {
            ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
                String message = fieldError.getDefaultMessage();
                res.addValidationError(fieldError.getField(), translator.translate(message));
            });
        }
        return res;
    }
}
