package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;
import org.example.Classes.User;

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

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
