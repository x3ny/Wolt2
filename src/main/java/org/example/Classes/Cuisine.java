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
@Table (name = "cuisines")

public class Cuisine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false,unique = true,length = 255)
    private String name;
    @Column(length = 1000)
    private String description;
    private boolean active;
    private LocalDateTime dateCreated;

    public Cuisine(String name, String description, boolean active){
        this.name = name;
        this.description = description;
        this.active = active;
    }

    public Cuisine(int id, String name, String description, boolean active,LocalDateTime dateCreated ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.dateCreated = dateCreated;
    }

    @PrePersist
    public void prePersist(){
        this.dateCreated = LocalDateTime.now();
    }
}
