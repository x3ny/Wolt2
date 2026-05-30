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

public class User extends BasicUser{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(nullable = false, unique = true, length = 100)
    private String username;
    @Column(nullable = false, unique = true, length = 150)
    private String email;
    @Column(nullable = false, length = 255)
    private String password;


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
