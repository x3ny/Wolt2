package org.example.services;

import org.example.Classes.FoodOrder;

public class OrderFactory {
    public FoodOrder createFoodOrder(int customerId, int driverId, int restaurantId, double totalPrice, String deliveryAddress, String paymentMethod, boolean paid){
         FoodOrder foodOrder = new FoodOrder();
         foodOrder.setCustomerId(customerId);
         foodOrder.setDriverId(driverId);
         foodOrder.setRestaurantId(restaurantId);
         foodOrder.setTotalPrice(totalPrice);
         foodOrder.setDeliveryAddress(deliveryAddress);
         foodOrder.setPaymentMethod(paymentMethod);
         foodOrder.setPaid(paid);

         return foodOrder;
    }
}
