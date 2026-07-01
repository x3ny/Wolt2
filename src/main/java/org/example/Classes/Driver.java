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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VehicleType vehicleType;
    @Column(length = 30)
    private String vehiclePlateNumber;
    private LocalDateTime dateCreated;

    public Driver(int id, String username, String email, String password, String firstName, String lastName, String phoneNumber, VehicleType vehicleType, String vehiclePlateNumber, LocalDateTime dateCreated) {
        super(id, username, email, password, firstName, lastName, phoneNumber);
        this.vehicleType = vehicleType;
        this.vehiclePlateNumber = vehiclePlateNumber;
        this.dateCreated = dateCreated;
    }
}
