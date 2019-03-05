package com.twitter.api.controller;

import com.twitter.api.dto.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionControllerAdvice {

    private static Logger log = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @Order(100)
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> validationExceptionHandler(MethodArgumentNotValidException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(ErrorResponse.from(exception));
    }


    @Order
    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity jsonSerializationExceptionHandler(HttpMessageNotReadableException exception) {

        log.error("Unable to serialize/deserialize", exception);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .contentType(MediaType.APPLICATION_JSON)
                             .body(ErrorResponse.of("Server is unable to process the request"));
    }

}
