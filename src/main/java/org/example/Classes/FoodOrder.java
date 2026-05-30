package org.example.Classes;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter

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
}
