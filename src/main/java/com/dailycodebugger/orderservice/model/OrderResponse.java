package com.dailycodebugger.orderservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private ProductResponse productResponse;

    private PaymentResponse paymentResponse;

    private long orderId;

    private long amount;

    private String orderStatus;

    private Instant orderDate;
}
