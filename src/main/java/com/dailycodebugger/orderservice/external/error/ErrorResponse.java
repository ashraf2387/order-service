package com.dailycodebugger.orderservice.external.error;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

    private String errorCode;

    private String errorMsg;
}
