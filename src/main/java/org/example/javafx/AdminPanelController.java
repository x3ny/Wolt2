package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.Classes.User;

import java.io.IOException;

public class AdminPanelController {
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Integer> idColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> loginColumn;
    @FXML
    private TableColumn<User, String> emailColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> phoneNumberColumn;
    @FXML
    private TableColumn<User, Boolean> activeColumn;
    @FXML
    private TableColumn<User, Boolean> adminColumn;
    @FXML
    private Label usersCountLabel;

    @Setter
    private EntityManagerFactory entityManagerFactory;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<>("login"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        phoneNumberColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        activeColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
        adminColumn.setCellValueFactory(new PropertyValueFactory<>("canViewUsers"));

        driverIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        driverUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        driverFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        driverLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        driverPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        driverEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        driverVehicleTypeColumn.setCellValueFactory(new PropertyValueFactory<>("vehicleType"));
        driverVehiclePlateColumn.setCellValueFactory(new PropertyValueFactory<>("vehiclePlateNumber"));

        restaurantIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        restaurantUsernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        restaurantNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        restaurantPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        restaurantEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        restaurantsAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        restaurantOpenColumn.setCellValueFactory(new PropertyValueFactory<>("open"));
        restaurantRatingColumn.setCellValueFactory(new PropertyValueFactory<>("rating"));

    }

    public void loadUsers() {
        if (entityManagerFactory == null) {
            return;
        }

        try (var entityManager = entityManagerFactory.createEntityManager()) {
            ObservableList<User> users = FXCollections.observableArrayList(
                    entityManager.createQuery("SELECT user FROM User user ORDER BY user.id", User.class)
                            .getResultList()
            );

            usersTable.setItems(users);
            usersCountLabel.setText("Users: " + users.size());
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Could not load users", "Please try again.");
        }
    }

    @FXML
    private void refreshUsers() {
        loadUsers();
    }

    @FXML
    private void addUser() {
        if(entityManagerFactory == null) {
            showAlert(Alert.AlertType.ERROR, "Could not load users", "Please try again.");
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register-view.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();
            controller.setEntityManagerFactory(entityManagerFactory);

            Stage stage = (Stage) usersTable.getScene().getWindow();
            stage.setTitle("Register");
            stage.setScene(new Scene(root, 750, 450));

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void deleteUser(){
        if (entityManagerFactory == null) {
            showAlert(Alert.AlertType.ERROR, "Could not delete user", "Database connection is not available.");
            return;
        }

        User user = usersTable.getSelectionModel().getSelectedItem();

        if(user == null){
            showAlert(Alert.AlertType.ERROR, "Select a user", "Please select a user.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete User");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete user: "  + user.getUsername());

        if(confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK){
            return;
        }

        try (var entityManager = entityManagerFactory.createEntityManager()) {
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                User userToDelete = entityManager.find(User.class, user.getId());
                if(userToDelete == null){
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Could not delete user", "The selected user no longer exists.");
                    loadUsers();
                    return;
                }

                entityManager.remove(userToDelete);
                transaction.commit();
                usersTable.getItems().remove(user);
                usersCountLabel.setText("Users: " + usersTable.getItems().size());
            } catch (RuntimeException exception) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                exception.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Could not delete user", "Please try again.");
            }

        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
