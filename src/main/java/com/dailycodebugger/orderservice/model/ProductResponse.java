package com.dailycodebugger.orderservice.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductResponse {

    private String productName;

    private long productId;

    private long price;

    private long quantity;
}
