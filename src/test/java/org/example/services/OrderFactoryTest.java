package org.example.services;

import org.example.Classes.FoodOrder;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderFactoryTest {

    @Test
    void factoryCopiesOrderValues() {
        OrderFactory orderFactory = new OrderFactory();

        FoodOrder foodOrder = orderFactory.createFoodOrder(
                1, 1, 1, 10, "asdasdasdasd", "cash", true
        );

        assertEquals(1, foodOrder.getCustomerId());
        assertEquals(1, foodOrder.getDriverId());
        assertEquals(1, foodOrder.getRestaurantId());
        assertEquals(10, foodOrder.getTotalPrice());
        assertEquals("asdasdasdasd", foodOrder.getDeliveryAddress());
        assertEquals("cash", foodOrder.getPaymentMethod());
        assertTrue(foodOrder.isPaid());
    }
}
