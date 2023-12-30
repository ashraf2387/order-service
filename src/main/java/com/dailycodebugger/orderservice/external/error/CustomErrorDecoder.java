package com.dailycodebugger.orderservice.external.error;

import com.dailycodebugger.orderservice.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;

import java.io.IOException;

public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            ErrorResponse errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
            return new CustomException(errorResponse.getErrorMsg(), errorResponse.getErrorCode(), String.valueOf(response.status()));
        } catch (IOException e) {
            return new CustomException("Internal server error", "INTERNAL_SERVER_ERROR", "500");
        }
    }
}
