package org.example.Classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

public class FoodOrder {
    private int id;
    private int customerId;
    private int restaurantId;
    private int driverId;
    private String deliveryAddress;
    private String status;
    private double totalPrice;
    private String paymentMethod;
    private boolean paid;
    private LocalDateTime dateCreated;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime deliveredAt;

    public FoodOrder(int id, int customerId, int restaurantId, int driverId, String deliveryAddress, String status, double totalPrice, String paymentMethod, boolean paid, LocalDateTime dateCreated, LocalDateTime estimatedDeliveryTime, LocalDateTime deliveredAt) {
        this.id = id;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId;
        this.deliveryAddress = deliveryAddress;
        this.status = status;
        this.totalPrice = totalPrice;
        this.paymentMethod = paymentMethod;
        this.paid = paid;
        this.dateCreated = dateCreated;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.deliveredAt = deliveredAt;
    }
}
