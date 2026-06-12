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
@Table (name = "users")

public class User extends BasicUser{

    private boolean canCreateUsers;
    private boolean canUpdateUsers;
    private boolean canDeleteUsers;
    private boolean canViewUsers;
    private LocalDateTime dateCreated;


    public User(boolean canCreateUsers, boolean canUpdateUsers, boolean canDeleteUsers, boolean canViewUsers) {
        this.canCreateUsers = canCreateUsers;
        this.canUpdateUsers = canUpdateUsers;
        this.canDeleteUsers = canDeleteUsers;
        this.canViewUsers = canViewUsers;
    }

    public User(int id, String username, String login, String email, String password, String firstName, String lastName, String phoneNumber, boolean canCreateUsers, boolean canUpdateUsers, boolean canDeleteUsers, boolean canViewUsers) {
        super(id, username, login, email, password, firstName, lastName, phoneNumber);
        this.canCreateUsers = canCreateUsers;
        this.canUpdateUsers = canUpdateUsers;
        this.canDeleteUsers = canDeleteUsers;
        this.canViewUsers = canViewUsers;
    }

    public User(int id, String username, String login, String email, String password, String firstName, String lastName, String phoneNumber, LocalDateTime dateCreated, LocalDateTime lastLogin, boolean active, boolean canCreateUsers, boolean canUpdateUsers, boolean canDeleteUsers, boolean canViewUsers) {
        super(id, username, login, email, password, firstName, lastName, phoneNumber, dateCreated, lastLogin, active);
        this.canCreateUsers = canCreateUsers;
        this.canUpdateUsers = canUpdateUsers;
        this.canDeleteUsers = canDeleteUsers;
        this.canViewUsers = canViewUsers;
    }

    public void prePersist(){
        this.dateCreated = LocalDateTime.now();
    }

}
