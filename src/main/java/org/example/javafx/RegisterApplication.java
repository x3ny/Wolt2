package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

import java.io.IOException;

public class RegisterApplication extends Application {
    private EntityManagerFactory entityManagerFactory;

    @Override
    public void start(Stage stage) throws IOException {
        entityManagerFactory = Persistence.createEntityManagerFactory("eternal_blue2");

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/register-view.fxml"));
        Parent root = loader.load();
        RegisterController controller = loader.getController();
        controller.setEntityManagerFactory(entityManagerFactory);

        stage.setTitle("Register");
        stage.setScene(new Scene(root, 400, 350));
        stage.show();
    }

    @Override
    public void stop() {
        if (entityManagerFactory != null) {
            entityManagerFactory.close();
        }
    }

}
