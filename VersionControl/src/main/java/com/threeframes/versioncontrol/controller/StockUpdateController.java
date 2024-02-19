package com.threeframes.versioncontrol.controller;

import com.threeframes.versioncontrol.payload.stock.Order;
import com.threeframes.versioncontrol.service.OrderProcessingEngineService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/order")
@Slf4j
public class StockUpdateController {
    private final OrderProcessingEngineService orderProcessingEngineService;

    public StockUpdateController(OrderProcessingEngineService orderProcessingEngineService) {
        this.orderProcessingEngineService = orderProcessingEngineService;
    }

    @PostMapping("/orders")
    public ResponseEntity<String> placeOrder(@RequestBody Order order) {
        orderProcessingEngineService.processOrder(order);
        orderProcessingEngineService.shutdown();
        log.info("Total order processed -> : {}",orderProcessingEngineService.getTotalProcessedOrders());
        return ResponseEntity.ok(String.valueOf(orderProcessingEngineService.getTotalProcessedOrders()));
    }
}
