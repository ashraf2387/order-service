package com.dailycodebugger.orderservice.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final String errorCode;
    private final String errorStatus;

    public CustomException(String message, String errorCode, String errorStatus) {
        super(message);
        this.errorCode = errorCode;
        this.errorStatus = errorStatus;
    }
}
