package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class RegisterApplication extends Application {
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void start(Stage stage) {
        entityManagerFactory = Persistence.createEntityManagerFactory("eternal_blue2");

        TextField usernameField = new TextField();
        TextField emailField = new TextField();
        PasswordField passwordField = new PasswordField();
        PasswordField confirmPasswordField = new PasswordField();
        Button registerButton = new Button("Register");
        Label messageLabel = new Label();

        registerButton.setOnAction(event -> {
            if (usernameField.getText().isBlank()
                    || emailField.getText().isBlank()
                    || passwordField.getText().isBlank()
                    || confirmPasswordField.getText().isBlank()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }

            if (!passwordField.getText().equals(confirmPasswordField.getText())) {
                messageLabel.setText("Passwords do not match.");
                return;
            }

            messageLabel.setText("Registration form is valid.");
        });

        GridPane form = new GridPane();
        form.setPadding(new Insets(20));
        form.setHgap(10);
        form.setVgap(10);

        form.add(new Label("Username:"), 0, 0);
        form.add(usernameField, 1, 0);
        form.add(new Label("Email:"), 0, 1);
        form.add(emailField, 1, 1);
        form.add(new Label("Password:"), 0, 2);
        form.add(passwordField, 1, 2);
        form.add(new Label("Confirm password:"), 0, 3);
        form.add(confirmPasswordField, 1, 3);
        form.add(registerButton, 1, 4);
        form.add(messageLabel, 1, 5);

        stage.setTitle("Register");
        stage.setScene(new Scene(form, 400, 280));
        stage.show();
    }

    @Override
    public void stop() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }
}
