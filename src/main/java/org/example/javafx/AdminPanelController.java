package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.Classes.Driver;
import org.example.Classes.Restaurant;
import org.example.Classes.User;
import org.example.Classes.VehicleType;

import java.io.IOException;

public class AdminPanelController {
    @FXML
    public Label driversCountLabel;
    @FXML
    public TableView<Driver> driversTable;
    @FXML
    public TableColumn<Driver, Integer> driverIdColumn;
    @FXML
    public TableColumn<Driver, String> driverUsernameColumn;
    @FXML
    public TableColumn<Driver, String> driverFirstNameColumn;
    @FXML
    public TableColumn<Driver, String> driverLastNameColumn;
    @FXML
    public TableColumn<Driver, String> driverPhoneColumn;
    @FXML
    public TableColumn<Driver, String> driverEmailColumn;
    @FXML
    public TableColumn<Driver, VehicleType> driverVehicleTypeColumn;
    @FXML
    public TableColumn<Driver, String> driverVehiclePlateColumn;
    @FXML
    public Label restaurantsCountLabel;
    @FXML
    public TableView<Restaurant> restaurantsTable;
    @FXML
    public TableColumn<Restaurant, Integer> restaurantIdColumn;
    @FXML
    public TableColumn<Restaurant, String> restaurantUsernameColumn;
    @FXML
    public TableColumn<Restaurant, String> restaurantNameColumn;
    @FXML
    public TableColumn<Restaurant, String> restaurantPhoneColumn;
    @FXML
    public TableColumn<Restaurant, String> restaurantEmailColumn;
    @FXML
    public TableColumn<Restaurant, String> restaurantsAddressColumn;
    @FXML
    public TableColumn<Restaurant, Boolean> restaurantOpenColumn;
    @FXML
    public TableColumn<Restaurant, Double> restaurantRatingColumn;
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
        loadDrivers();
        loadRestaurants();
    }

    public void loadDrivers() {
        if (entityManagerFactory == null) {
            return;
        }

        try (var entityManager = entityManagerFactory.createEntityManager()) {
            ObservableList<Driver> drivers = FXCollections.observableArrayList(
                    entityManager.createQuery("SELECT driver FROM Driver driver ORDER BY driver.id", Driver.class)
                            .getResultList()
            );

            driversTable.setItems(drivers);
            driversCountLabel.setText("Drivers: " + drivers.size());
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Could not load drivers", "Please try again.");
        }
    }

    public void loadRestaurants() {
        if (entityManagerFactory == null) {
            return;
        }

        try (var entityManager = entityManagerFactory.createEntityManager()) {
            ObservableList<Restaurant> restaurants = FXCollections.observableArrayList(
                    entityManager.createQuery("SELECT restaurant FROM Restaurant restaurant ORDER BY restaurant.id", Restaurant.class)
                            .getResultList()
            );

            restaurantsTable.setItems(restaurants);
            restaurantsCountLabel.setText("Restaurants: " + restaurants.size());
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Could not load restaurants", "Please try again.");
        }
    }

    @FXML
    private void refreshUsers() {
        loadUsers();
        loadRestaurants();
        loadDrivers();
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
        Driver driver = driversTable.getSelectionModel().getSelectedItem();
        Restaurant restaurant = restaurantsTable.getSelectionModel().getSelectedItem();

        String selectedType;

        if(user != null){
            selectedType = "USER";
        }else if(driver != null){
            selectedType = "DRIVER";
        }else if(restaurant != null){
            selectedType = "RESTAURANT";
        }else {
            selectedType = "NONE";
        }

        switch (selectedType) {
            case "USER" -> deleteSelectedUser(user);
            case "DRIVER" -> deleteSelectedDriver(driver);
            case "RESTAURANT" -> deleteSelectedRestaurant(restaurant);
            default -> showAlert(Alert.AlertType.ERROR, "Select item" , "Please select a user, driver or restaurant.");
        }

    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    public void editUser(ActionEvent actionEvent) {
        User selectedUser = usersTable.getSelectionModel().getSelectedItem();
        Driver selectedDriver = driversTable.getSelectionModel().getSelectedItem();
        Restaurant selectedRestaurant = restaurantsTable.getSelectionModel().getSelectedItem();

        String selectedType;

        if(selectedUser != null){
            selectedType = "USER";
        }else if(selectedDriver != null){
            selectedType = "DRIVER";
        }else if(selectedRestaurant != null){
            selectedType = "RESTAURANT";
        }else {
            selectedType = "NONE";
        }

        switch (selectedType) {
            case "USER":
            System.out.println("Selected user: " + selectedUser.getUsername());
            break;

            case "DRIVER":
            System.out.println("Selected driver: " + selectedDriver.getUsername());
            break;

            case "RESTAURANT":
            System.out.println("Selected restaurant: " + selectedRestaurant.getUsername());
            break;

            default:
            showAlert(Alert.AlertType.ERROR, "Selected user", "Please select a user.");
            return;
        }

        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/register-view.fxml"));
            Parent root = loader.load();

            RegisterController controller = loader.getController();

            switch (selectedType) {
                case "USER" -> {
                    controller.setEntityManagerFactory(entityManagerFactory);
                    controller.setUserToEdit(selectedUser);
                }
                case "DRIVER" -> {
                    controller.setEntityManagerFactory(entityManagerFactory);
                    controller.setDriverToEdit(selectedDriver);
                }
                case "RESTAURANT" -> {
                    controller.setEntityManagerFactory(entityManagerFactory);
                    controller.setRestaurantToEdit(selectedRestaurant);
                }
            }


            Stage stage = (Stage) usersTable.getScene().getWindow();
            stage.setTitle("Edit user");
            stage.setScene(new Scene(root,750,450));
        } catch (IOException exception){
            exception.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Could not load user", "Please try again.");
        }
    }
    private void deleteSelectedUser(User user){
        Alert confirm  = new  Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete user");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete user: " + user.getUsername());

        if(confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK){
            return;
        }
        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();

                User userToDelete = entityManager.find(User.class, user.getId());

                if(userToDelete == null){
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Could not delete user", "Please try again.");
                    loadUsers();
                    return;
                }

                entityManager.remove(userToDelete);
                transaction.commit();

                usersTable.getItems().remove(user);
                usersCountLabel.setText("Users: " + usersTable.getItems().size());
            }catch(RuntimeException exception){
                if(transaction.isActive()) {
                    transaction.rollback();
                }

                exception.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Could not delete", "Please try again.");
            }
        }

    }

    private void deleteSelectedDriver(Driver driver){
        Alert confirm  = new  Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete driver");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete driver: " + driver.getUsername());

        if(confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK){
            return;
        }
        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();

                Driver driverToDelete = entityManager.find(Driver.class, driver.getId());

                if(driverToDelete == null){
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Could not delete driver", "Please try again.");
                    loadDrivers();
                    return;
                }

                entityManager.remove(driverToDelete);
                transaction.commit();

                driversTable.getItems().remove(driver);
                driversCountLabel.setText("Drivers: " + driversTable.getItems().size());
            }catch(RuntimeException exception){
                if(transaction.isActive()) {
                    transaction.rollback();
                }

                exception.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Could not delete", "Please try again.");
            }
        }

    }

    private void deleteSelectedRestaurant(Restaurant restaurant){
        Alert confirm  = new  Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Delete restaurant");
        confirm.setHeaderText(null);
        confirm.setContentText("Delete restaurant: " + restaurant.getUsername());

        if(confirm.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK){
            return;
        }
        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();

                Restaurant restaurantToDelete = entityManager.find(Restaurant.class, restaurant.getId());

                if(restaurantToDelete == null){
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Could not delete restaurant", "Please try again.");
                    loadRestaurants();
                    return;
                }

                entityManager.remove(restaurantToDelete);
                transaction.commit();

                restaurantsTable.getItems().remove(restaurant);
                restaurantsCountLabel.setText("Restaurants: " + restaurantsTable.getItems().size());
            }catch(RuntimeException exception){
                if(transaction.isActive()) {
                    transaction.rollback();
                }

                exception.printStackTrace();
                showAlert(Alert.AlertType.ERROR, "Could not delete", "Please try again.");
            }
        }

    }

}
