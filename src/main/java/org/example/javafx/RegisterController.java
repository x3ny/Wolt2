package org.example.javafx;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class RegisterController {
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
    }
}
