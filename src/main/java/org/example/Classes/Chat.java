package org.example.Classes;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter

public class Chat {
    private int id;
    private int orderId;
    private int customerId;
    private int restaurantId;
    private int driverId;
    private String message;
    private LocalDateTime dateCreated;
    private List<String> messages;
}

