package org.example.Classes;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "menu_items")

public class MenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false)
    private int restaurantId;
    @Column(nullable = false, length = 150)
    private String name;
    private String description;
    @Column(nullable = false)
    private double price;
    private boolean available = true;
    public MenuItem(int restaurantId, String name, String description, double price, boolean available) {
        this.restaurantId = restaurantId;
        this.name = name;
        this.description = description;
        this.price = price;
        this.available = available;
    }
}
