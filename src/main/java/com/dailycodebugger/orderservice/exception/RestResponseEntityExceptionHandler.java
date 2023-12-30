package com.dailycodebugger.orderservice.exception;

import com.dailycodebugger.orderservice.external.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException e) {

        ErrorResponse response = ErrorResponse.builder()
                .errorCode(e.getErrorCode())
                .errorMsg(e.getMessage())
                .build();

        return new ResponseEntity<>(response, HttpStatus.valueOf(e.getErrorCode()));
    }
}
