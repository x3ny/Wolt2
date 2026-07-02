package org.example.javafx;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;
import org.example.Classes.FoodOrder;
import org.example.Classes.OrderStatus;
import org.example.Classes.Restaurant;

public class RestaurantController {
    @FXML
    private TextField customerIdField;
    @FXML
    private TextField driverIdField;
    @FXML
    private TextField deliveryAddressTextField;
    @FXML
    private TextField totalPriceTextField;
    @FXML
    private TextField paymentMethodTextField;
    @FXML
    private Button createOrderButton;
    @FXML
    private CheckBox paidCheckBox;
    @FXML
    private TableView <FoodOrder> foodOrdersTable;
    @FXML
    private TableColumn <FoodOrder, Integer> idColumn;
    @FXML
    private TableColumn <FoodOrder, Integer> customerIdColumn;
    @FXML
    private TableColumn <FoodOrder, Integer> restaurantIdColumn;
    @FXML
    private TableColumn <FoodOrder, Integer> driverIdColumn;
    @FXML
    private TableColumn <FoodOrder ,String> deliveryAddressColumn;
    @FXML
    private TableColumn <FoodOrder, OrderStatus> orderStatusColumn;
    @FXML
    private TableColumn <FoodOrder, Double> totalPriceColumn;
    @FXML
    private TableColumn <FoodOrder, String> paymentMethodColumn;
    @FXML
    private TableColumn <FoodOrder, Boolean> paidColumn;


    @Setter
    private EntityManagerFactory entityManagerFactory;

    private Restaurant currentRestaurant;

    public void setCurrentRestaurant(Restaurant currentRestaurant) {
        this.currentRestaurant = currentRestaurant;
        loadOrders();
    }

    @FXML
    private void initialize(){
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerIdColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        restaurantIdColumn.setCellValueFactory(new PropertyValueFactory<>("restaurantId"));
        driverIdColumn.setCellValueFactory(new PropertyValueFactory<>("driverId"));
        deliveryAddressColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryAddress"));
        orderStatusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        paymentMethodColumn.setCellValueFactory(new PropertyValueFactory<>("paymentMethod"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));
    }

    public void loadOrders() {
        if (entityManagerFactory == null) {
            return;
        }
        if (currentRestaurant == null) {
            return;
        }

        try(var entityManager = entityManagerFactory.createEntityManager()){
            ObservableList<FoodOrder> foodOrders = FXCollections.observableArrayList(entityManager.createQuery("""
            SELECT foodOrder
            FROM FoodOrder foodOrder
            WHERE foodOrder.restaurantId = :restaurantId
            ORDER BY foodOrder.dateCreated DESC
        
        """, FoodOrder.class).setParameter("restaurantId", currentRestaurant.getId()).getResultList());
            foodOrdersTable.setItems(foodOrders);

        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void createOrder(ActionEvent actionEvent) {
        if(entityManagerFactory == null || currentRestaurant == null) {
            return;
        }

        String customerIdText = customerIdField.getText().trim();
        String driverIdText = driverIdField.getText().trim();
        String deliveryAddressText = deliveryAddressTextField.getText().trim();
        String paymentMethodText = paymentMethodTextField.getText().trim();
        String totalPriceText = totalPriceTextField.getText().trim();

        if(customerIdText.isBlank()
            || driverIdText.isBlank()
            || deliveryAddressText.isBlank()
            || paymentMethodText.isBlank()
            || totalPriceText.isBlank()){

            showAlert(Alert.AlertType.ERROR, "Fill in all the fields" , "Please fill all the fields");

            return;
        }

        try {
            int customerId = Integer.parseInt(customerIdText);
            int driverId = Integer.parseInt(driverIdText);
            double totalPrice = Double.parseDouble(totalPriceText);

            FoodOrder foodOrder = new FoodOrder();
            foodOrder.setCustomerId(customerId);
            foodOrder.setRestaurantId(currentRestaurant.getId());
            foodOrder.setDriverId(driverId);
            foodOrder.setDeliveryAddress(deliveryAddressText);
            foodOrder.setPaymentMethod(paymentMethodText);
            foodOrder.setTotalPrice(totalPrice);
            foodOrder.setPaid(paidCheckBox.isSelected());

            saveOrder(foodOrder);
            clearOrderForm();
            loadOrders();


        }catch (NumberFormatException e){
            showAlert(Alert.AlertType.WARNING, "Invalid number" , "Customer ID, driver ID and total price must be a valid number");
        }
        catch (RuntimeException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Could not create order", "please try again.");
        }



    }

    private void showAlert(Alert.AlertType alertType, String title, String message){
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

    private void saveOrder(FoodOrder foodOrder) {
        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                entityManager.persist(foodOrder);
                transaction.commit();
            }catch (RuntimeException e){
                if(transaction.isActive()){
                    transaction.rollback();
                }
                throw e;
            }
        }

    }

    private void clearOrderForm(){
        customerIdField.clear();
        driverIdField.clear();
        deliveryAddressTextField.clear();
        paymentMethodTextField.clear();
        totalPriceTextField.clear();
        paidCheckBox.setSelected(false);

    }
}
