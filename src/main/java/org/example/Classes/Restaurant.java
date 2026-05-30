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

public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, length = 150)
    private String name;
    @Column(length = 1000)
    private String description;
    @Column(length = 30)
    private String phoneNumber;
    @Column(length = 150)
    private String email;
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

    public Restaurant(String name, String description, String phoneNumber, String email, String address, int cuisineId, boolean open, double rating, LocalDateTime dateCreated) {
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

    @PrePersist
    public void prePersist(){
        this.dateCreated = LocalDateTime.now();
    }
}
