package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginApplication extends Application {
    @FXML
    public TextField UsernameField;
    @FXML
    public TextField PasswordField;
    @FXML
    public Button LoginButton;
    @FXML
    public Button RegisterButton;

    @FXML
    private void openRegisterForm() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register-view.fxml"));
        Parent root = loader.load();

        RegisterController controller = loader.getController();
        controller.setEntityManagerFactory(entityManagerFactory);

        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        stage.setTitle("Register");
        stage.setScene(new Scene(root, 750, 450));
    }

    private EntityManagerFactory entityManagerFactory;

    @Override
    public void start(Stage stage) throws IOException {
        entityManagerFactory = Persistence.createEntityManagerFactory("eternal_blue2");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login-view.fxml"));
        Parent root = loader.load();

        LoginApplication controller = loader.getController();
        controller.setEntityManagerFactory(entityManagerFactory);

        stage.setTitle("Login");
        stage.setScene(new Scene(root, 450, 300));
        stage.show();
    }

    public void setEntityManagerFactory(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public void stop(){
        if(entityManagerFactory != null){
            entityManagerFactory.close();
        }
    }

}
