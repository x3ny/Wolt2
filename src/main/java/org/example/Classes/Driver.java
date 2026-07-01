package org.example.Classes;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@Entity
@Table (name = "drivers")

public class Driver extends BasicUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private int userId;
    @Column(nullable = false, length = 100)
    private String username;
    @Column(nullable = false, length = 150)
    private String password;
    @Column(nullable = false, length = 100)
    private String firstName;
    @Column(nullable = false, length = 100)
    private String lastName;
    @Column(nullable = false, length = 30)
    private String phoneNumber;
    @Column(length = 150)
    private String email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleType vehicleType;
    @Column(length = 30)
    private String vehiclePlateNumber;
    private LocalDateTime dateCreated;

    public Driver(int id, int userId, String firstName, String lastName, String phoneNumber, String email, VehicleType vehicleType, String vehiclePlateNumber, LocalDateTime dateCreated){
        this.id = id;
        this. userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.vehicleType = vehicleType;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.dateCreated = dateCreated;
    }

    public Driver(int userId, String username, String password, String firstName, String lastName, String phoneNumber, String email, VehicleType vehicleType, String vehiclePlateNumber, LocalDateTime dateCreated){
        this. userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.vehicleType = vehicleType;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.dateCreated = dateCreated;
    }

    @PrePersist
    public void prePersist(){
        this.dateCreated = LocalDateTime.now();
    }

}
