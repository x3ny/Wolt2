package org.example.javafx;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginApplication extends Application {
    @FXML
    public TextField UsernameField;
    @FXML
    public TextField PasswordField;
    @FXML
    public Button LoginButton;
    @FXML
    public Button RegisterButton;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

    }
}
