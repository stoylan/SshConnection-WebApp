package com.ssh.api.controller;

import com.ssh.core.utilities.results.ErrorDataResult;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.security.sasl.AuthenticationException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@CrossOrigin
public class GlobalControllerExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDataResult<Object> handleValidationException(MethodArgumentNotValidException exceptions) {
        Map<String, String> validationErrors = new HashMap<>();
        for (FieldError fieldError : exceptions.getBindingResult().getFieldErrors()) {
            validationErrors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        return new ErrorDataResult<>(validationErrors, "Validation Errors!");
    }

    @ExceptionHandler({BadCredentialsException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorDataResult<Object> handleUnAuthorizedException(BadCredentialsException exception) {
        return new ErrorDataResult<>(exception.getLocalizedMessage(), "Unauthorized please login and try again.");
    }

    @ExceptionHandler({AuthenticationException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDataResult<Object> failed(AuthenticationException exception) {
        return new ErrorDataResult<>(exception.getLocalizedMessage(), "Unauthorized please login and try again.");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDataResult<Object> handle(NoHandlerFoundException ex) {
        return new ErrorDataResult<>(ex.getLocalizedMessage(), "Something happened");
    }
}
