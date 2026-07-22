package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import lombok.Setter;
import org.example.Classes.*;
import org.example.Classes.MenuItem;
import org.example.validation.OrderValidator;
import org.hibernate.query.Order;

import java.time.LocalDateTime;

public class RestaurantController {
    public Button deleteOrder;
    @FXML
    public Label currentRestaurantLabel;
    @FXML
    public TextField menuItemNameTextField;
    @FXML
    public TextField menuItemDescriptionTextField;
    @FXML
    public TextField menuItemPriceTextField;
    @FXML
    public CheckBox menuItemAvailableCheckBox;
    public Button addMenuItemButton;
    public TableView <MenuItem> menuItemsTable;
    public TableColumn <MenuItem, String> nameColumn;
    public TableColumn <MenuItem, String> descriptionColumn;
    public TableColumn <MenuItem, Double> priceColumn;
    public TableColumn <MenuItem, Boolean> availableColumn;
    @FXML
    public TextField quantityTextField;
    @FXML
    public TableView <OrderItem> orderItemsTable;
    @FXML
    public TableColumn <OrderItem, Integer> orderItemMenuIdColumn;
    @FXML
    public TableColumn <OrderItem, Integer> orderItemQuantityColumn;
    @FXML
    public TableColumn <OrderItem, Double> orderItemUnitPriceColumn;
    @FXML
    public TableColumn <MenuItem, String> orderItemMenuNameColumn;
    public Label currentRestaurantMenuLabel;
    @FXML
    private ComboBox <User> customerIdComboBox;
    @FXML
    private ComboBox <Driver> driverIdComboBox;
    @FXML
    private ComboBox <PaymentMethod> paymentMethodComboBox;
    @FXML
    private TableColumn <FoodOrder, LocalDateTime> dateCreated;
    @FXML
    private TextField deliveryAddressTextField;
    @FXML
    private TextField totalPriceTextField;
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

    OrderValidator orderValidator = new OrderValidator();

    @Setter
    private EntityManagerFactory entityManagerFactory;

    private Restaurant currentRestaurant;

    public void setCurrentRestaurant(Restaurant currentRestaurant) {
        this.currentRestaurant = currentRestaurant;
        loadOrders();
        loadComboBoxData();
        loadMenuItems();
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
        dateCreated.setCellValueFactory(new  PropertyValueFactory<>("dateCreated"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        availableColumn.setCellValueFactory(new PropertyValueFactory<>("available"));
        orderItemMenuIdColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemId"));
        orderItemMenuNameColumn.setCellValueFactory(new PropertyValueFactory<>("menuItemName"));
        orderItemQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orderItemUnitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        menuItemsTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        foodOrdersTable.getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldOrder, selectedOrder) -> {
                    if (selectedOrder != null) {
                        loadOrderItems(selectedOrder.getId());
                    }
                });
        configureUserComboBox(customerIdComboBox, "Customer");
        configureUserComboBox(driverIdComboBox, "Driver");

        paymentMethodComboBox.getItems().addAll(PaymentMethod.values());

    }

    private void loadOrderItems(int id) {
        try (var entityManager = entityManagerFactory.createEntityManager()) {
            ObservableList<OrderItem> orderItems =
                    FXCollections.observableArrayList(
                            entityManager.createQuery("""
                          SELECT orderItem
                          FROM OrderItem orderItem
                          WHERE orderItem.foodOrderId = :foodOrderId
                          ORDER BY orderItem.id
                      """, OrderItem.class)
                                    .setParameter("foodOrderId", id)



                                    .getResultList()
                    );

            orderItemsTable.setItems(orderItems);

            for(OrderItem orderItem : orderItems){
                MenuItem menuItem = entityManager.find(MenuItem.class, orderItem.getMenuItemId());
                if(menuItem != null){
                    orderItem.setMenuItemName(menuItem.getName());
                }
            }

        }


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
            currentRestaurantLabel.setText("Welcome " + currentRestaurant.getRestaurantName() + "!");

        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    public void loadMenuItems(){
        if (entityManagerFactory == null) {
            return;
        }
        if (currentRestaurant == null) {
            return;
        }

        try(var entityManager = entityManagerFactory.createEntityManager()){
            ObservableList<MenuItem> menuItems = FXCollections.observableArrayList(entityManager.createQuery("""
            SELECT menuItems
            FROM MenuItem menuItems
            WHERE menuItems.restaurantId = :restaurantId
        
        """, MenuItem.class).setParameter("restaurantId", currentRestaurant.getId()).getResultList());
            menuItemsTable.setItems(menuItems);

        }catch (RuntimeException e){
            e.printStackTrace();
        }
    }

    @FXML
    public void createOrder() {
        if(entityManagerFactory == null || currentRestaurant == null) {
            return;
        }

        User selectedCustomer = customerIdComboBox.getValue();
        Driver selectedDriver = driverIdComboBox.getValue();
        PaymentMethod paymentMethod = paymentMethodComboBox.getValue();

        ObservableList<MenuItem> menuItems = menuItemsTable.getSelectionModel().getSelectedItems();

        if(menuItems.isEmpty()){
            showAlert(Alert.AlertType.ERROR, "Select menu items", "Please select at least one menu item!");
            return;
        }

        String quantityText = quantityTextField.getText().trim();

        if(quantityText.isBlank()){
            showAlert(Alert.AlertType.ERROR, "Please enter a valid quantity", "Quantity cannot be blank!");
            return;
        }

        int quantity;

        try{
            quantity = Integer.parseInt(quantityText);
        }catch (NumberFormatException e){
            showAlert(Alert.AlertType.ERROR, "Please enter a valid quantity", "Quantity must be a whole number!");
            return;
        }

        if(quantity <= 0){
            showAlert(Alert.AlertType.ERROR, "Invalid quantity", "Quantity must be greater than 0");
            return;
        }


        double totalPrice = menuItems.stream().mapToDouble(menuItem -> menuItem.getPrice() * quantity ).sum();

        if(selectedCustomer == null || selectedDriver == null || paymentMethod == null) {
            showAlert(Alert.AlertType.ERROR, "Select Customer, Driver and Payment Method", "Please select Customer, Driver and Payment Method");
            return;
        }

        int customerId = selectedCustomer.getId();
        int driverId = selectedDriver.getId();
        String deliveryAddressText = deliveryAddressTextField.getText().trim();

        if(deliveryAddressText.isBlank()){

            showAlert(Alert.AlertType.ERROR, "Fill in all the fields" , "Please fill all the fields");

            return;
        }

        try {

            if(!orderValidator.isAddressValid(deliveryAddressText)){
                showAlert(Alert.AlertType.ERROR, "Invalid Delivery Address length" , "Delivery Address length must be at least 10 characters");
                return;
            }

            FoodOrder foodOrder = createFoodOrder(customerId,driverId,totalPrice,deliveryAddressText, String.valueOf(paymentMethod),paidCheckBox.isSelected());
            saveOrder(foodOrder);
            for(MenuItem menuItem : menuItems){
                OrderItem orderItem = new OrderItem(foodOrder.getId(), menuItem.getId(), quantity,menuItem.getPrice());
                saveOrderItem(orderItem);
            }
            clearOrderForm();
            loadOrders();


        }catch (RuntimeException e){
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
        customerIdComboBox.setValue(null);
        driverIdComboBox.setValue(null);
        deliveryAddressTextField.clear();
        //totalPriceTextField.clear();
        paymentMethodComboBox.setValue(PaymentMethod.CARD);
        paidCheckBox.setSelected(false);

    }

    @FXML
    private void cancelSelectedOrder() {
        updateSelectedOrderStatus(OrderStatus.CANCELED);
    }
    @FXML
    private void markSelectedOrderDelivered() {
        updateSelectedOrderStatus(OrderStatus.DELIVERED);
    }
    @FXML
    private void markSelectedOrderPreparing() {
        updateSelectedOrderStatus(OrderStatus.PREPARING);
    }
    @FXML
    private void acceptSelectedOrder() {
        updateSelectedOrderStatus(OrderStatus.ACCEPTED);
    }
    private void updateSelectedOrderStatus(OrderStatus orderStatus) {

        FoodOrder selectedOrder = foodOrdersTable.getSelectionModel().getSelectedItem();

        if(selectedOrder == null){
            showAlert(Alert.AlertType.ERROR, "Select an order", "Please select an order");
            return;
        }

        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                FoodOrder orderToUpdate = entityManager.find(FoodOrder.class, selectedOrder.getId());
                if(orderToUpdate == null){
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Order not found", "The selected order does not exist");
                    loadOrders();
                    return;
                }
                if (orderToUpdate.getRestaurantId() != currentRestaurant.getId()) {
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Access denied", "This order does not belong to your restaurant");
                    loadOrders();
                    return;
                }

                orderToUpdate.setStatus(orderStatus);
                transaction.commit();
                loadOrders();
            }catch (RuntimeException e){
                if(transaction.isActive()){
                    transaction.rollback();
                }
                throw e;
            }


        } catch (RuntimeException e){
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR,"Could not update order", "Please try again");
        }

    }
    private FoodOrder createFoodOrder(int customerId, int driverId, double totalPrice, String deliveryAddress, String paymentMethod, boolean  paid){
            FoodOrder foodOrder = new FoodOrder();
            foodOrder.setCustomerId(customerId);
            foodOrder.setDriverId(driverId);
            foodOrder.setTotalPrice(totalPrice);
            foodOrder.setDeliveryAddress(deliveryAddress);
            foodOrder.setPaymentMethod(paymentMethod);
            foodOrder.setPaid(paid);
            foodOrder.setRestaurantId(currentRestaurant.getId());

            return foodOrder;
    }
    public void deleteOrder(ActionEvent actionEvent) {
        FoodOrder selectedOrder = foodOrdersTable.getSelectionModel().getSelectedItem();

        if(selectedOrder == null){
            showAlert(Alert.AlertType.WARNING, "Select an order", "Please select an order");
            return;
        }
        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();

                FoodOrder orderToDelete = entityManager.find(FoodOrder.class, selectedOrder.getId());

                if(orderToDelete == null){
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Order not found", "The selected order does not exist");
                    return;
                }

                if(orderToDelete.getRestaurantId() != currentRestaurant.getId()){
                    transaction.rollback();
                    showAlert(Alert.AlertType.ERROR, "Access denied", "This order does not belong to your restaurant");
                    loadOrders();
                    return;
                }
                entityManager.remove(orderToDelete);
                transaction.commit();
                showAlert(Alert.AlertType.INFORMATION, "Order deleted", "Order deleted successfully");
                loadOrders();


            }catch (RuntimeException e){
                if(transaction.isActive()){
                    transaction.rollback();
                }
                throw e;
            }
        }
    }
    private void loadComboBoxData(){
        try(var entityManager = entityManagerFactory.createEntityManager()){
            customerIdComboBox.getItems().setAll(entityManager.createQuery("SELECT u FROM User u", User.class).getResultList());

            driverIdComboBox.getItems().setAll(entityManager.createQuery("SELECT d FROM Driver d", Driver.class).getResultList());
        }
    }
    private String formatUserComboBoxText(BasicUser user){
        return  user.getId() + " - " + user.getFirstName() + " - " + user.getLastName() + " - " + user.getEmail();
    }
    private <T extends BasicUser> void configureUserComboBox(ComboBox<T> comboBox, String placeholder){
        comboBox.setCellFactory(listView -> new  ListCell<>() {
            @Override
            protected void updateItem(T user, boolean empty) {
                super.updateItem(user, empty);

                if(empty || user == null){
                    setText(null);
                } else {
                    setText(formatUserComboBoxText(user));
                }
            }
        });

        comboBox.setButtonCell(new ListCell<>(){
            @Override
            protected void updateItem(T user, boolean empty) {
                super.updateItem(user, empty);

                if(empty || user == null){
                    setText(placeholder);
                }else
                    setText(formatUserComboBoxText(user));
            }
        });


    }


    @FXML
    public void createMenuItem(ActionEvent actionEvent) {
        if(entityManagerFactory == null || currentRestaurant == null) {
            return;
        }

        String menuItemNameText = menuItemNameTextField.getText().trim();
        String menuItemDescriptionText =  menuItemDescriptionTextField.getText().trim();
        String menuItemPriceText = menuItemPriceTextField.getText().trim();

        if(menuItemPriceText.isBlank() || menuItemNameText.isBlank()){
            showAlert(Alert.AlertType.ERROR, "Missing fields" , "Name and price are required");
            return;
        }

        try{
            double menuItemPrice = Double.parseDouble(menuItemPriceText);

            if(!orderValidator.isTotalPriceValid(menuItemPrice)){
                showAlert(Alert.AlertType.ERROR, "Invalid Menu Item Price" , "Total Price must be greater than 0");
                return;
            }

            MenuItem menuItem = new MenuItem(
                    currentRestaurant.getId(),
                    menuItemNameText,
                    menuItemDescriptionText,
                    menuItemPrice,
                    menuItemAvailableCheckBox.isSelected()
            );

            saveMenuItem(menuItem);
            loadMenuItems();


        }catch (NumberFormatException e){
            showAlert(Alert.AlertType.WARNING, "Invalid number" , "Price must be a valid number");
            return;
        }


    }

    private void saveMenuItem(MenuItem menuItem) {
        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                entityManager.persist(menuItem);
                transaction.commit();
            }catch (RuntimeException e){
                if(transaction.isActive()){
                    transaction.rollback();
                }
                throw e;
            }
        }

    }

    private void saveOrderItem(OrderItem orderItem) {
        try(var entityManager = entityManagerFactory.createEntityManager()){
            var transaction = entityManager.getTransaction();
            try{
                transaction.begin();
                entityManager.persist(orderItem);
                transaction.commit();
            }catch (RuntimeException e){
                if(transaction.isActive()){
                    transaction.rollback();
                }
                throw e;
            }
        }

    }
}
