package com.kbtg.bootcamp.posttest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.time.ZonedDateTime;

@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class })
    public ResponseEntity<Object> handleNotFoundException(NotFoundException notFoundException){

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                notFoundException.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {InternalServiceException.class})
    public ResponseEntity<Object> handleInternalServiceException(InternalServiceException internalServiceException){

        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                internalServiceException.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {BadRequestException.class})
    public ResponseEntity<Object> handleBadRequestException(BadRequestException badRequestException) {
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                badRequestException.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity<Object> handleValidationException(ValidationException ex) {
        String[] errorMessages = ex.getMessage().split(", ");
        ApiExceptionResponse apiExceptionResponse = new ApiExceptionResponse(
                String.join("", errorMessages),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );

        return new ResponseEntity<>(apiExceptionResponse, HttpStatus.BAD_REQUEST);
    }
}
