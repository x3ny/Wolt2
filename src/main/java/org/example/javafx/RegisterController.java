package org.example.javafx;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import lombok.Setter;
import org.example.Classes.User;
import org.example.Database.GenericHibernate;

public class RegisterController {
    @FXML
    private Button registerButton;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private TextField lastNameField;
    @FXML
    private TextField firstNameField;
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
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (username.isBlank()
                || email.isBlank()
                || password.isBlank()
                || confirmPassword.isBlank()
                || firstName.isBlank()
                || lastName.isBlank()
                || phoneNumber.isBlank()) {
            messageLabel.setText("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            return;
        }

        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
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
        firstNameField.clear();
        lastNameField.clear();
        phoneNumberField.clear();
        usernameField.clear();
        emailField.clear();
        passwordField.clear();
        confirmPasswordField.clear();
    }
}
