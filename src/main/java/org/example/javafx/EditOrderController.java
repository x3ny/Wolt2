package org.example.javafx;

import jakarta.persistence.EntityManagerFactory;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Setter;
import org.example.Classes.FoodOrder;
import org.example.Classes.OrderStatus;
import org.example.Classes.PaymentMethod;
import org.example.Classes.Restaurant;
import org.example.Database.GenericHibernate;

import java.io.IOException;

public class EditOrderController{
    @FXML
    public TextField OrderPrice;
    @FXML
    public ComboBox <OrderStatus> OrderStatus;
    @FXML
    public ComboBox <PaymentMethod> OrderPaymentMethod;
    @FXML
    public CheckBox IsOrderPaid;
    @FXML
    public TextField OrderAddress;
    @FXML
    public Button saveButton;
    @Setter
    private EntityManagerFactory entityManagerFactory;

    @Setter
    private FoodOrder foodOrderToEdit;

    public void setOrderToEdit(FoodOrder foodOrder){
        foodOrderToEdit = foodOrder;

        OrderPrice.setText(String.valueOf(foodOrder.getTotalPrice()));
        OrderStatus.setValue(foodOrder.getStatus());
        OrderPaymentMethod.setValue(PaymentMethod.valueOf(foodOrder.getPaymentMethod()));
        IsOrderPaid.setSelected(foodOrder.isPaid());
        OrderAddress.setText(foodOrder.getDeliveryAddress());
    }

    public void SaveEditedOrder(ActionEvent actionEvent) throws IOException {
        FoodOrder foodOrder = new FoodOrder();
        foodOrder.setTotalPrice(Double.parseDouble(OrderPrice.getText()));
        foodOrder.setStatus(OrderStatus.getValue());
        foodOrder.setPaymentMethod(String.valueOf(PaymentMethod.valueOf(foodOrderToEdit.getPaymentMethod())));
        foodOrder.setDeliveryAddress(OrderAddress.getText());
        foodOrder.setPaid(IsOrderPaid.isSelected());


        GenericHibernate<FoodOrder> foodOrderHibernate = new GenericHibernate<>(entityManagerFactory, FoodOrder.class);
        foodOrderHibernate.update(foodOrder);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/restaurant_view.fxml"));
        Parent root = loader.load();

        RestaurantController controller = loader.getController();
        controller.setEntityManagerFactory(entityManagerFactory);
        //controller.setCurrentRestaurant(restaurant);

        Stage stage = (Stage) saveButton.getScene().getWindow();
        stage.setTitle("Restaurant panel");
        stage.setScene(new Scene(root, 1200, 700));
    }
}
