package org.example.Classes;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter

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


}
