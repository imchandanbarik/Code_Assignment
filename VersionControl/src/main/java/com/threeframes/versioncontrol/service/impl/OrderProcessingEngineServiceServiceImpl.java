package com.threeframes.versioncontrol.service.impl;

import com.threeframes.versioncontrol.payload.stock.Order;
import com.threeframes.versioncontrol.payload.stock.Warehouse;
import com.threeframes.versioncontrol.service.OrderProcessingEngineService;
import com.threeframes.versioncontrol.utils.RadialDistanceCalculator;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class OrderProcessingEngineServiceServiceImpl implements OrderProcessingEngineService {
    private ConcurrentHashMap<String, Warehouse> warehouses;
    private ExecutorService executorService;
    private AtomicInteger totalProcessedOrders;
    private static final String ORDER_TOPIC = "orders";

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    public OrderProcessingEngineServiceServiceImpl() {
        this.warehouses = new ConcurrentHashMap<>();
        this.executorService = Executors.newCachedThreadPool();
        this.totalProcessedOrders = new AtomicInteger(0);
    }
    @PostConstruct
    public void init() {
        // Initialize warehouses and populate with initial stock levels
        Warehouse warehouse1 = new Warehouse("Warehouse_1", 37.7749, -122.4194); // San Francisco, CA
        warehouse1.getStock().put("Product_A", 1000);
        warehouse1.getStock().put("Product_B", 1500);
        warehouses.put(warehouse1.getWarehouseId(), warehouse1);

        Warehouse warehouse2 = new Warehouse("Warehouse_2", 34.0522, -118.2437); // Los Angeles, CA
        warehouse2.getStock().put("Product_A", 800);
        warehouse2.getStock().put("Product_B", 1200);
        warehouses.put(warehouse2.getWarehouseId(), warehouse2);

    }
    @Override
    public void processOrder(Order order) {
            executorService.submit(() -> {
                // Find the nearest warehouse with available stock
                Warehouse nearestWarehouse = findNearestWarehouse(order);
                if (nearestWarehouse != null && nearestWarehouse.canFulfillOrder(order)) {
                    // Process order in the selected warehouse
                    kafkaTemplate.send(ORDER_TOPIC,order);
                    /** commented this line as we are performing this through kafka
                        nearestWarehouse.processOrder(order);
                     **/
                    totalProcessedOrders.incrementAndGet();
                } else {
                    // Handle scenario when no warehouse can fulfill the order
                    System.out.println("No warehouse can fulfill the order " + order.getOrderId() + " at the moment.");
                }
            });
        }

    private Warehouse findNearestWarehouse(Order order) {
        Warehouse nearestWarehouse = null;
        double minDistance = Double.MAX_VALUE;

        for (Warehouse warehouse : warehouses.values()) {
            double distance = RadialDistanceCalculator.calculateDistance(order, warehouse);
            if (distance < minDistance) {
                minDistance = distance;
                nearestWarehouse = warehouse;
            }
        }
        return nearestWarehouse;
    }
    @Override
    public void shutdown() {
        executorService.shutdown();
    }

    @Override
    public int getTotalProcessedOrders() {
        return totalProcessedOrders.get();
    }
}
