package com.threeframes.versioncontrol.service;

import com.threeframes.versioncontrol.payload.stock.Order;
import com.threeframes.versioncontrol.payload.stock.Warehouse;
import org.springframework.stereotype.Service;

@Service
public interface OrderProcessingEngineService {
    void processOrder(Order order);
    void shutdown();

    int getTotalProcessedOrders();

}
