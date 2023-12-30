package com.dailycodebugger.orderservice.service;

import com.dailycodebugger.orderservice.model.OrderRequest;
import com.dailycodebugger.orderservice.model.OrderResponse;

import java.util.List;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);

    List<OrderResponse> getAllOrderDetails();
}
