package com.threeframes.versioncontrol.payload.stock;

import com.threeframes.versioncontrol.payload.stock.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Warehouse {
    private String warehouseId;
    private double latitude;
    private double longitude;
    private ConcurrentHashMap<String, Integer> stock;
    private Lock stockLock;

    public Warehouse(String warehouseId, double latitude, double longitude) {
        this.warehouseId = warehouseId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.stock = new ConcurrentHashMap<>();
        this.stockLock = new ReentrantLock();
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ConcurrentHashMap<String, Integer> getStock() {
        return stock;
    }

    @KafkaListener(topics = "orders", groupId = "warehouse-group")
    public void processOrder(Order order) {
        boolean orderFulfilled = false;
        try {
            stockLock.lock(); // Acquire lock before updating stock levels
            // Check if the warehouse has sufficient stock for the order
            if (canFulfillOrder(order)) {
                // Update stock levels and mark order as fulfilled
                stock.compute(order.getProductId(), (productId, currentStock) -> currentStock - order.getQuantity());
                orderFulfilled = true;
            }
        } finally {
            stockLock.unlock(); // Release lock after updating stock levels
        }

        if (orderFulfilled) {
            System.out.println("Order " + order.getOrderId() + " processed in Warehouse " + warehouseId);
            // Further processing such as generating invoices, updating order status, etc.
        } else {
            // Handle scenario when the warehouse cannot fulfill the order
            System.out.println("Order " + order.getOrderId() + " cannot be fulfilled in Warehouse " + warehouseId
                    + " due to insufficient stock.");
            // Potentially notify the customer or trigger a backorder process
        }
    }

    public boolean canFulfillOrder(Order order) {
        // Check if the warehouse has sufficient stock for the order
        return stock.getOrDefault(order.getProductId(), 0) >= order.getQuantity();
    }
}
