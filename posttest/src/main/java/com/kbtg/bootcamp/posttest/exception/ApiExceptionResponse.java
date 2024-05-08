package com.kbtg.bootcamp.posttest.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ApiExceptionResponse {

    private final String message;
    private final HttpStatus httpStatus;
    private final ZonedDateTime dateTime;

}

