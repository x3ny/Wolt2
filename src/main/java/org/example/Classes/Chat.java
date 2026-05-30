package org.example.Classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

public class Chat {
    private int id;
    private int orderId;
    private int customerId;
    private int restaurantId;
    private int driverId;
    private String message;
    private LocalDateTime dateCreated;
    private List<String> messages;

    public Chat(int id, int orderId, int customerId, int restaurantId, int driverId, String message, LocalDateTime dateCreated, List<String> messages ){
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId;
        this.message = message;
        this.dateCreated = dateCreated;
        this.messages = messages;
    }
}

