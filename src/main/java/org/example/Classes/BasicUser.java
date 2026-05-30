package org.example.Classes;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor

public class BasicUser {
    private int id;
    private String username;
    private String login;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private LocalDateTime dateCreated;
    private LocalDateTime lastLogin;
    private boolean active;

    public BasicUser(int id, String username, String login, String email, String password,
                     String firstName, String lastName, String phoneNumber) {
        this.id = id;
        this.username = username;
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateCreated = LocalDateTime.now();
        this.active = true;
    }

    public BasicUser(int id, String username, String login, String email, String password,
                     String firstName, String lastName, String phoneNumber,
                     LocalDateTime dateCreated, LocalDateTime lastLogin, boolean active) {
        this.id = id;
        this.username = username;
        this.login = login;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.dateCreated = dateCreated;
        this.lastLogin = lastLogin;
        this.active = active;
    }


    @Override
    public String toString() {
        return "BasicUser{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dateCreated=" + dateCreated +
                ", lastLogin=" + lastLogin +
                ", active=" + active +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof BasicUser basicUser)) {
            return false;
        }
        return id == basicUser.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
