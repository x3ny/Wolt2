package org.example.javafx;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.example.Classes.User;
import org.example.Database.GenericHibernate;

public class RegisterController {
    @Setter
    private EntityManagerFactory entityManagerFactory;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;

    @FXML
    private Label messageLabel;


    @FXML
    private void register() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isBlank()
                || email.isBlank()
                || password.isBlank()
                || confirmPassword.isBlank()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        User user = new User();
        user.setUsername(username);
        user.setLogin(username);
        user.setEmail(email);
        user.setPassword(password);

        try {
            GenericHibernate<User> userHibernate = new GenericHibernate<>(entityManagerFactory, User.class);
            userHibernate.create(user);
            clearForm();
            messageLabel.setText("User registered successfully");
        } catch (RuntimeException exception){
            exception.printStackTrace();
            messageLabel.setText("Could not register user. Username or email may already exist");
        }
    }


    private void clearForm() {
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
