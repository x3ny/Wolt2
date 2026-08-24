package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.Classes.Driver;
import org.example.Classes.FoodOrder;
import org.example.Classes.MenuItem;
import org.example.Classes.OrderItem;
import org.example.Classes.Restaurant;
import org.example.Classes.PaymentMethod;

import java.io.IOException;

public class EditOrderController {
    @FXML public TextField OrderPrice;
    @FXML public ComboBox<org.example.Classes.OrderStatus> OrderStatus;
    @FXML public ComboBox<PaymentMethod> OrderPaymentMethod;
    @FXML public CheckBox IsOrderPaid;
    @FXML public TextField OrderAddress;
    @FXML public Button saveButton;
    @FXML private ComboBox<Driver> driverComboBox;
    @FXML private ComboBox<MenuItem> menuItemComboBox;
    @FXML private TextField menuItemQuantityTextField;
    @FXML private TableView<OrderItem> orderItemsTable;
    @FXML private TableColumn<OrderItem, String> menuItemNameColumn;
    @FXML private TableColumn<OrderItem, Integer> quantityColumn;
    @FXML private TableColumn<OrderItem, Double> unitPriceColumn;

    @Setter private EntityManagerFactory entityManagerFactory;
    @Setter private FoodOrder foodOrderToEdit;

    @Setter private Restaurant restaurantToReturn;

    private final ObservableList<OrderItem> editedOrderItems =
            FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        OrderStatus.getItems().addAll(org.example.Classes.OrderStatus.values());
        OrderPaymentMethod.getItems().addAll(PaymentMethod.values());

        menuItemNameColumn.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getMenuItemName()));
        quantityColumn.setCellValueFactory(cell ->
                new SimpleIntegerProperty(cell.getValue().getQuantity()).asObject());
        unitPriceColumn.setCellValueFactory(cell ->
                new SimpleDoubleProperty(cell.getValue().getUnitPrice()).asObject());
        orderItemsTable.setItems(editedOrderItems);

        driverComboBox.setCellFactory(list -> driverCell());
        driverComboBox.setButtonCell(driverCell());
        menuItemComboBox.setCellFactory(list -> menuItemCell());
        menuItemComboBox.setButtonCell(menuItemCell());
    }

    public void setOrderToEdit(FoodOrder foodOrder) {
        foodOrderToEdit = foodOrder;
        OrderPrice.setText(String.valueOf(foodOrder.getTotalPrice()));
        OrderStatus.setValue(foodOrder.getStatus());
        OrderPaymentMethod.setValue(PaymentMethod.valueOf(foodOrder.getPaymentMethod()));
        IsOrderPaid.setSelected(foodOrder.isPaid());
        OrderAddress.setText(foodOrder.getDeliveryAddress());

        try (var entityManager = entityManagerFactory.createEntityManager()) {
            driverComboBox.getItems().setAll(entityManager.createQuery(
                    "SELECT d FROM Driver d ORDER BY d.firstName", Driver.class
            ).getResultList());
            driverComboBox.setValue(entityManager.find(Driver.class, foodOrder.getDriverId()));

            editedOrderItems.setAll(entityManager.createQuery("""
                    SELECT item FROM OrderItem item
                    WHERE item.foodOrderId = :orderId ORDER BY item.id
                    """, OrderItem.class)
                    .setParameter("orderId", foodOrder.getId())
                    .getResultList());

            for (OrderItem item : editedOrderItems) {
                MenuItem menuItem = entityManager.find(MenuItem.class, item.getMenuItemId());
                if (menuItem != null) item.setMenuItemName(menuItem.getName());
            }

            menuItemComboBox.getItems().setAll(entityManager.createQuery(
                    "SELECT m FROM MenuItem m WHERE m.restaurantId = :restaurantId ORDER BY m.name",
                    MenuItem.class
            ).setParameter("restaurantId", foodOrder.getRestaurantId()).getResultList());
        }
    }

    @FXML
    public void SaveEditedOrder(ActionEvent actionEvent) throws IOException {
        if (foodOrderToEdit == null || driverComboBox.getValue() == null) {
            showError("Select a driver", "Please select a driver before saving.");
            return;
        }

        try (var entityManager = entityManagerFactory.createEntityManager()) {
            var transaction = entityManager.getTransaction();
            try {
                transaction.begin();
                FoodOrder order = entityManager.find(FoodOrder.class, foodOrderToEdit.getId());
                order.setDriverId(driverComboBox.getValue().getId());
                order.setStatus(OrderStatus.getValue());
                order.setPaymentMethod(OrderPaymentMethod.getValue().name());
                order.setDeliveryAddress(OrderAddress.getText().trim());
                order.setPaid(IsOrderPaid.isSelected());
                order.setTotalPrice(editedOrderItems.stream()
                        .mapToDouble(item -> item.getQuantity() * item.getUnitPrice()).sum());

                entityManager.createQuery(
                        "DELETE FROM OrderItem item WHERE item.foodOrderId = :orderId")
                        .setParameter("orderId", order.getId()).executeUpdate();

                for (OrderItem item : editedOrderItems) {
                    item.setId(0);
                    item.setFoodOrderId(order.getId());
                    entityManager.persist(item);
                }
                transaction.commit();
            } catch (RuntimeException exception) {
                if (transaction.isActive()) transaction.rollback();
                throw exception;
            }
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/restaurant_view.fxml"));

        Parent root = loader.load();

        RestaurantController controller = loader.getController();
        controller.setEntityManagerFactory(entityManagerFactory);
        controller.setCurrentRestaurant(restaurantToReturn);


        try (var entityManager = entityManagerFactory.createEntityManager()) {
            Restaurant restaurant = entityManager.find(
                    Restaurant.class, foodOrderToEdit.getRestaurantId());
            controller.setCurrentRestaurant(restaurant);
        }
        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.setTitle("Restaurant panel");
        stage.setScene(new Scene(root, 1200, 700));
    }

    @FXML
    private void addMenuItem() {
        MenuItem menuItem = menuItemComboBox.getValue();
        try {
            int quantity = Integer.parseInt(menuItemQuantityTextField.getText().trim());
            if (menuItem == null || quantity <= 0) throw new NumberFormatException();
            OrderItem item = new OrderItem(
                    foodOrderToEdit.getId(), menuItem.getId(), quantity, menuItem.getPrice());
            item.setMenuItemName(menuItem.getName());
            editedOrderItems.add(item);
            menuItemQuantityTextField.clear();
        } catch (NumberFormatException exception) {
            showError("Invalid menu item", "Select an item and enter a positive quantity.");
        }
    }

    @FXML
    private void removeMenuItem() {
        OrderItem selected = orderItemsTable.getSelectionModel().getSelectedItem();
        if (selected != null) editedOrderItems.remove(selected);
    }

    private ListCell<Driver> driverCell() {
        return new ListCell<>() {
            @Override protected void updateItem(Driver driver, boolean empty) {
                super.updateItem(driver, empty);
                setText(empty || driver == null ? null :
                        driver.getId() + " - " + driver.getFirstName() + " " + driver.getLastName());
            }
        };
    }

    private ListCell<MenuItem> menuItemCell() {
        return new ListCell<>() {
            @Override protected void updateItem(MenuItem item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : item.getName() + " - " + item.getPrice());
            }
        };
    }

    private void showError(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
