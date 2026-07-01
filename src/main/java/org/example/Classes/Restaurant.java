package org.example.Classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "restaurants")

public class Restaurant extends BasicUser {
    @Column(nullable = false, length = 150)
    private String restaurantName;
    @Column(length = 1000)
    private String description;
    @Column(nullable = false, length = 255)
    private String address;
    @Column(nullable = false)
    private int cuisineId;
    private boolean open;
    private double rating;
    private LocalDateTime dateCreated;

    /* Situos fieldus gal veliau bus galima panaudot
    private String imageUrl;
    private double deliveryFee;
    private int estimatedDeliveryMinutes;
     */
    public Restaurant(int id, String username, String email, String password, String firstName, String lastName, String phoneNumber, String restaurantName, String description, String address, int cuisineId, boolean open, double rating, LocalDateTime dateCreated) {
        super(id, username, email, password, firstName, lastName, phoneNumber);
        this.restaurantName = restaurantName;
        this.description = description;
        this.address = address;
        this.cuisineId = cuisineId;
        this.open = open;
        this.rating = rating;
        this.dateCreated = dateCreated;
    }

}
