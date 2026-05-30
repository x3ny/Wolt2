package org.example.Classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Cuisine {
    private int id;
    private String name;
    private String description;
    private boolean active;

    public Cuisine(int id, String name, String description, boolean active ){
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
    }
}
