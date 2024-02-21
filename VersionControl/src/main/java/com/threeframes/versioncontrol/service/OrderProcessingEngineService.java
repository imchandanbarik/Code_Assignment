package com.threeframes.versioncontrol.service;

import com.threeframes.versioncontrol.payload.stock.Order;
import org.springframework.stereotype.Service;

@Service
public interface OrderProcessingEngineService {
    void processOrder(Order order);
    void shutdown();

    int getTotalProcessedOrders();

}
