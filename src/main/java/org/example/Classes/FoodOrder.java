package org.example.Classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "food_orders")

public class FoodOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int customerId;
    @Column(nullable = false)
    private int restaurantId;
    private int driverId;
    @Column(nullable = false, length = 255)
    private String deliveryAddress;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private OrderStatus status;
    private double totalPrice;
    private String paymentMethod;
    private boolean paid;
    private LocalDateTime dateCreated;
    private LocalDateTime estimatedDeliveryTime;
    private LocalDateTime deliveredAt;

    public FoodOrder(int id, int customerId, int restaurantId, int driverId, String deliveryAddress, OrderStatus status, double totalPrice, String paymentMethod, boolean paid, LocalDateTime dateCreated, LocalDateTime estimatedDeliveryTime, LocalDateTime deliveredAt) {
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

    public FoodOrder(int customerId, int restaurantId, int driverId, String deliveryAddress, OrderStatus status, double totalPrice, String paymentMethod, boolean paid, LocalDateTime dateCreated, LocalDateTime estimatedDeliveryTime, LocalDateTime deliveredAt) {
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

    public FoodOrder(FoodOrder foodOrder) {
    }

    @PrePersist
    public void prePersist(){
        this.dateCreated = LocalDateTime.now();
        if(this.status == null){
            this.status = OrderStatus.CREATED;
        }
    }


    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "FoodOrder{" +
                "dateCreated=" + formatter.format(dateCreated) +
                '}';
    }
}
