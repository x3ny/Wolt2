package org.example.Classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor

public class Driver {
    private int id;
    private int userId;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String vehicleType;
    private String vehiclePlateNumber;
    private LocalDateTime dateCreated;

    public Driver(int id, int userId, String firstName, String lastName, String phoneNumber, String email, String vehicleType, String vehiclePlateNumber, LocalDateTime dateCreated){
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

}
