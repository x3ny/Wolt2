package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Persistence;
import jakarta.persistence.TypedQuery;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.Classes.Restaurant;
import org.example.Classes.User;

import java.io.IOException;
import java.time.LocalDateTime;

public class LoginApplication extends Application {
    @FXML
    public TextField UsernameField;
    @FXML
    public PasswordField PasswordField;
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

    @Setter
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

    @Override
    public void stop(){
        if(entityManagerFactory != null){
            entityManagerFactory.close();
        }
    }

    public void authenticate(ActionEvent actionEvent) {
        String username = UsernameField.getText().trim();
        String password = PasswordField.getText();

        if (username.isBlank() || password.isBlank()){
            showAlert(Alert.AlertType.WARNING, "Missing credentials", "Please enter both username and password.");
            return;
        }

        try(var entityManager = entityManagerFactory.createEntityManager()){
            TypedQuery<Restaurant> restaurantQuery = entityManager.createQuery(
                    "SELECT restaurant FROM Restaurant restaurant WHERE restaurant.username = :login AND restaurant.password = :password ",
                    Restaurant.class);

            restaurantQuery.setParameter("login", username);
            restaurantQuery.setParameter("password", password);

            Restaurant restaurant = restaurantQuery.getSingleResult();
            openRestaurantPanel(restaurant);
            return;
        } catch (NoResultException ignored) {
        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login Failed", "Login Failed");
            return;
        }

        try(var entityManager = entityManagerFactory.createEntityManager()) {


            TypedQuery<User> query = entityManager.createQuery(
                    "SELECT user FROM User user WHERE user.username = :login AND user.password = :password ",
                    User.class
            );



            query.setParameter("login", username);
            query.setParameter("password", password);

            User user = query.getSingleResult();

            var transaction = entityManager.getTransaction();

            try{
                transaction.begin();
                user.setLastLogin(LocalDateTime.now());
                entityManager.merge(user);
                transaction.commit();
            }catch (RuntimeException exception){
                if(transaction.isActive()){
                    transaction.rollback();
                }
                throw exception;
            }

            showAlert(Alert.AlertType.INFORMATION, "Login successful", "Welcome, " + user.getUsername() + "!");

            if (user.isCanViewUsers()) {
                try {
                    openAdminPanel();
                } catch (IOException exception) {
                    exception.printStackTrace();
                    showAlert(Alert.AlertType.ERROR, "Could not open admin panel", "Please try again.");
                }
            }

        } catch (NoResultException exception){
            showAlert(Alert.AlertType.ERROR, "Login failed", "Invalid username or password.");
        } catch (RuntimeException exception){
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Login failed", "Could not log in. Please try again.");
        }


    }

    private void openAdminPanel() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/admin-panel-view.fxml"));
        Parent root = loader.load();

        AdminPanelController controller = loader.getController();
        controller.setEntityManagerFactory(entityManagerFactory);
        controller.loadUsers();
        controller.loadDrivers();
        controller.loadRestaurants();

        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        stage.setTitle("Admin panel");
        stage.setScene(new Scene(root, 900, 900));
    }

    private void openRestaurantPanel(Restaurant restaurant) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/restaurant_view.fxml"));
        Parent root = loader.load();

        RestaurantController controller = loader.getController();
        controller.setEntityManagerFactory(entityManagerFactory);
        controller.setCurrentRestaurant(restaurant);

        Stage stage = (Stage) RegisterButton.getScene().getWindow();
        stage.setTitle("Restaurant panel");
        stage.setScene(new Scene(root, 1200, 500));




    }

    private void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}
