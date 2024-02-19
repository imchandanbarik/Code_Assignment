package com.threeframes.versioncontrol.payload.stock;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@AllArgsConstructor
public class OrderProcessingRequest {
    private ConcurrentHashMap<String, Warehouse> warehouses; // WarehouseId -> Warehouse
    private ExecutorService executorService;
    private AtomicInteger totalProcessedOrders;
}
