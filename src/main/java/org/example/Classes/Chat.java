package org.example.Classes;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "chats")
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private int orderId;

    @Column(nullable = false)
    private int customerId;

    @Column(nullable = false)
    private int restaurantId;

    private int driverId;

    @Column(length = 1000)
    private String message;

    private LocalDateTime dateCreated;

    @ElementCollection
    @CollectionTable(name = "chat_messages", joinColumns = @JoinColumn(name = "chat_id"))
    @Column(name = "message", length = 1000)
    private List<String> messages;

    public Chat(int orderId, int customerId, int restaurantId, int driverId,
                String message, List<String> messages) {
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId;
        this.message = message;
        this.messages = messages;
    }

    public Chat(int id, int orderId, int customerId, int restaurantId, int driverId,
                String message, LocalDateTime dateCreated, List<String> messages) {
        this.id = id;
        this.orderId = orderId;
        this.customerId = customerId;
        this.restaurantId = restaurantId;
        this.driverId = driverId;
        this.message = message;
        this.dateCreated = dateCreated;
        this.messages = messages;
    }

    @PrePersist
    public void prePersist() {
        this.dateCreated = LocalDateTime.now();
    }
}

