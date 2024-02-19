package com.threeframes.versioncontrol.payload.stock;

import lombok.Data;

@Data
public class Order {
    private String orderId;
    private String productId;
    private int quantity;
    private double deliveryLatitude;
    private double deliveryLongitude;
}
