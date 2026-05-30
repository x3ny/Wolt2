package org.example.Classes;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class User extends BasicUser{
    private boolean canCreateUsers;
    private boolean canUpdateUsers;
    private boolean canDeleteUsers;
    private boolean canViewUsers;

}
