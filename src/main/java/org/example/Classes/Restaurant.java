package org.example.Classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor

public class Restaurant {
    private int id;
    private String name;
    private String description;
    private String phoneNumber;
    private String email;
    private String address;
    private int cuisineId;
    private boolean open;
    private double rating;
    private LocalDateTime dateCreated;

    /* Situos fieldus gal veliau bus galima panaudot
    private String imageUrl;
    private double deliveryFee;
    private int estimatedDeliveryMinutes;
     */

    public Restaurant(int id, String name, String description, String phoneNumber, String email, String address, int cuisineId, boolean open, double rating, LocalDateTime dateCreated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
        this.cuisineId = cuisineId;
        this.open = open;
        this.rating = rating;
        this.dateCreated = dateCreated;
    }
}
