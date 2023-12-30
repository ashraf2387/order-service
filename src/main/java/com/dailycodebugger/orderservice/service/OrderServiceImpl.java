package com.dailycodebugger.orderservice.service;

import com.dailycodebugger.orderservice.entity.Order;
import com.dailycodebugger.orderservice.exception.CustomException;
import com.dailycodebugger.orderservice.external.client.PaymentService;
import com.dailycodebugger.orderservice.external.client.ProductService;
import com.dailycodebugger.orderservice.model.*;
import com.dailycodebugger.orderservice.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    public static final String PRODUCT_SERVICE_BASE_URL = "http://PRODUCT-SERVICE/product/";
    public static final String PAYMENT_SERVICE_BASE_URL = "http://PAYMENT-SERVICE/payment/";
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

    @Override
    @Transactional
    public long placeOrder(OrderRequest orderRequest) {
        log.info("Placing order request : {}", orderRequest);
        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("COMPLETED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        order = orderRepository.save(order);

        PaymentRequest paymentRequest = PaymentRequest
                .builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .referenceNumber(UUID.randomUUID().toString())
                .amount(orderRequest.getTotalAmount())
                .build();

        String orderStatus;
        try {
            paymentService.doPayment(paymentRequest);
            orderStatus = "PLACED";
        } catch (Exception e) {
            orderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(orderStatus);

        log.info("Order places successfully with order id : {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new CustomException("Order not found", "NOT_FOUND", "404"));

        ProductResponse productResponse = restTemplate.getForObject(PRODUCT_SERVICE_BASE_URL + order.getProductId(), ProductResponse.class);
        PaymentResponse paymentResponse = restTemplate.getForObject(PAYMENT_SERVICE_BASE_URL + order.getId(), PaymentResponse.class);
        return getOrderResponse(order, productResponse, paymentResponse);
    }

    @Override
    public List<OrderResponse> getAllOrderDetails() {
        return orderRepository.findAll().stream()
                .map(o -> {
                    ProductResponse productResponse = restTemplate.getForObject(PRODUCT_SERVICE_BASE_URL + o.getProductId(), ProductResponse.class);
                    PaymentResponse paymentResponse = restTemplate.getForObject(PAYMENT_SERVICE_BASE_URL + o.getId(), PaymentResponse.class);
                    return getOrderResponse(o, productResponse, paymentResponse);
                }).collect(Collectors.toList());
    }

    private OrderResponse getOrderResponse(Order order, ProductResponse productResponse, PaymentResponse paymentResponse) {
        return OrderResponse.builder()
                .orderId(order.getId())
                .orderStatus(order.getOrderStatus())
                .orderDate(order.getOrderDate())
                .amount(order.getAmount())
                .productResponse(productResponse)
                .paymentResponse(paymentResponse)
                .build();
    }
}
