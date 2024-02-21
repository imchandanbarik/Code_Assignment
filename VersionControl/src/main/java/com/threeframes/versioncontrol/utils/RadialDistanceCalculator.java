package com.threeframes.versioncontrol.utils;

import com.threeframes.versioncontrol.payload.stock.Order;
import com.threeframes.versioncontrol.payload.stock.Warehouse;

public class RadialDistanceCalculator {
    public static double calculateDistance(Order order, Warehouse warehouse) {
        double lat1 = order.getDeliveryLatitude();
        double lon1 = order.getDeliveryLongitude();
        double lat2 = warehouse.getLatitude();
        double lon2 = warehouse.getLongitude();

        // Haversine formula to calculate distance between two points on a sphere
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = 6371 * c; // Radius of Earth in kilometers

        return distance;
    }
}
