package org.example.javafx;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import lombok.Setter;
import org.example.Classes.Driver;
import org.example.Classes.Restaurant;
import org.example.Classes.User;
import org.example.Classes.VehicleType;
import org.example.Database.GenericHibernate;

public class RegisterController {
    @FXML
    private Label lastNameFieldLabel;
    @FXML
    private Label firstNameFieldLabel;
    @FXML
    private Label vehiclePlateNumberLabel;
    @FXML
    private Label vehicleTypeLabel;
    @FXML
    private Label restaurantDescriptionLabel;
    @FXML
    private Label restaurantAddressLabel;
    @FXML
    private Label restaurantNameLabel;
    @FXML
    private TextField vehiclePlateNumberField;
    @FXML
    private ComboBox<VehicleType> vehicleTypeComboBox;
    @FXML
    private TextField restaurantDescriptionField;
    @FXML
    private TextField restaurantAddressField;
    @FXML
    private TextField restaurantNameField;
    @FXML
    private RadioButton userRadioButton;
    @FXML
    private RadioButton restaurantRadioButton;
    @FXML
    private RadioButton driverRadioButton;
    @FXML
    private RadioButton adminRadioButton;
    public ToggleGroup accountTypeGroup;
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
    private void initialize(){
        vehicleTypeComboBox.getItems().addAll(VehicleType.values());

        updateVisibleFields();

        userRadioButton.setOnAction(event -> updateVisibleFields() );
        adminRadioButton.setOnAction(event -> updateVisibleFields() );
        restaurantRadioButton.setOnAction(event -> updateVisibleFields() );
        driverRadioButton.setOnAction(event -> updateVisibleFields() );
    }

    private void updateVisibleFields(){
        boolean driverSelected = driverRadioButton.isSelected();
        boolean restaurantSelected = restaurantRadioButton.isSelected();

        setDriverFieldsVisible(driverSelected);
        setRestaurantFieldsVisible(restaurantSelected);
    }

    private void setDriverFieldsVisible(boolean visible){
        vehiclePlateNumberField.setVisible(visible);
        vehiclePlateNumberField.setManaged(visible);

        vehicleTypeComboBox.setVisible(visible);
        vehicleTypeComboBox.setManaged(visible);

        vehiclePlateNumberLabel.setVisible(visible);
        vehiclePlateNumberLabel.setManaged(visible);

        vehicleTypeLabel.setVisible(visible);
        vehicleTypeLabel.setManaged(visible);
    }

    private void setRestaurantFieldsVisible(boolean visible){
        restaurantAddressField.setVisible(visible);
        restaurantAddressField.setManaged(visible);

        restaurantDescriptionField.setVisible(visible);
        restaurantDescriptionField.setManaged(visible);

        restaurantNameField.setVisible(visible);
        restaurantNameField.setManaged(visible);

        restaurantAddressLabel.setVisible(visible);
        restaurantAddressLabel.setManaged(visible);

        restaurantDescriptionLabel.setVisible(visible);
        restaurantDescriptionLabel.setManaged(visible);

        restaurantNameLabel.setVisible(visible);
        restaurantNameLabel.setManaged(visible);

        firstNameField.setVisible(!visible);
        firstNameField.setManaged(!visible);

        firstNameFieldLabel.setVisible(!visible);
        firstNameFieldLabel.setManaged(!visible);

        lastNameField.setVisible(!visible);
        lastNameField.setManaged(!visible);

        lastNameFieldLabel.setVisible(!visible);
        lastNameFieldLabel.setManaged(!visible);

    }


    @FXML
    private void register() {
        String firstName = firstNameField.getText().trim();
        String lastName = lastNameField.getText().trim();
        String phoneNumber = phoneNumberField.getText().trim();
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        String vehiclePlateNumber = vehiclePlateNumberField.getText();
        VehicleType vehicleType = vehicleTypeComboBox.getValue();
        String restaurantAddress = restaurantAddressField.getText();
        String restaurantDescription = restaurantDescriptionField.getText();
        String restaurantName = restaurantNameField.getText();

        if(userRadioButton.isSelected() || adminRadioButton.isSelected()){
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
        }

        if(driverRadioButton.isSelected()){
            if(username.isBlank()
                || firstName.isBlank() ||
                    lastName.isBlank() ||
                    phoneNumber.isBlank() ||
                    email.isBlank() ||
                    password.isBlank() ||
                    confirmPassword.isBlank() ||
                    username.isBlank() ||
                    vehiclePlateNumber.isBlank()
            ){
                messageLabel.setText("Please fill in all fields.");
                return;
            }
        }

        if(restaurantRadioButton.isSelected()){
            if (email.isBlank()
                    || password.isBlank()
                    || confirmPassword.isBlank()
                    || phoneNumber.isBlank()
                    || username.isBlank()
                    || restaurantDescription.isBlank()
                    || restaurantAddress.isBlank()
                    || restaurantName.isBlank()) {
                messageLabel.setText("Please fill in all fields.");
                return;
            }
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

        if(restaurantRadioButton.isSelected()){
            Restaurant restaurant = new Restaurant();
            restaurant.setAddress(restaurantAddress);
            restaurant.setEmail(email);
            restaurant.setName(restaurantName);
            restaurant.setDescription(restaurantDescription);
            restaurant.setPhoneNumber(phoneNumber);
            restaurant.setUsername(username);
            restaurant.setPassword(username);

            GenericHibernate<Restaurant> restaurantHibernate = new GenericHibernate<>(entityManagerFactory, Restaurant.class);
            restaurantHibernate.create(restaurant);
            clearForm();
        } //creating restaurant user

        if(driverRadioButton.isSelected()){
            Driver driver = new Driver();
            driver.setEmail(email);
            driver.setFirstName(firstName);
            driver.setLastName(lastName);
            driver.setUsername(username);
            driver.setPassword(password);
            driver.setPhoneNumber(phoneNumber);
            driver.setVehiclePlateNumber(vehiclePlateNumber);
            driver.setVehicleType(vehicleType);

            GenericHibernate<Driver> driverHibernate = new GenericHibernate<>(entityManagerFactory, Driver.class);
            driverHibernate.create(driver);
            clearForm();
        } //creating driver user

        if(adminRadioButton.isSelected()){
            user.setCanCreateUsers(true);
            user.setCanDeleteUsers(true);
            user.setCanUpdateUsers(true);
            user.setCanViewUsers(true);
        }

        try {
            GenericHibernate<User> userHibernate = new GenericHibernate<>(entityManagerFactory, User.class);
            userHibernate.create(user);
            clearForm();
            messageLabel.setText("User registered successfully !");
        } catch (RuntimeException exception){
            exception.printStackTrace();
            //messageLabel.setText("Could not register user. Username or email may already exist");
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
