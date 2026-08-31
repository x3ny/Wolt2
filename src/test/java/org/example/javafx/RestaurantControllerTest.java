package org.example.javafx;

import org.junit.jupiter.api.Test;

import org.example.Classes.OrderStatus;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantControllerTest {

    @Test
    void allowsValidForwardTransitions() {
        RestaurantController controller = new RestaurantController();

        assertTrue(controller.canChangeStatus(OrderStatus.CREATED, OrderStatus.ACCEPTED));
        assertTrue(controller.canChangeStatus(OrderStatus.ACCEPTED, OrderStatus.PREPARING));
        assertTrue(controller.canChangeStatus(OrderStatus.PREPARING, OrderStatus.DELIVERED));
    }

    @Test
    void allowsCancellationBeforeDelivery() {
        RestaurantController controller = new RestaurantController();

        assertTrue(controller.canChangeStatus(OrderStatus.CREATED, OrderStatus.CANCELED));
        assertTrue(controller.canChangeStatus(OrderStatus.ACCEPTED, OrderStatus.CANCELED));
        assertTrue(controller.canChangeStatus(OrderStatus.PREPARING, OrderStatus.CANCELED));
    }

    @Test
    void rejectsInvalidAndTerminalTransitions() {
        RestaurantController controller = new RestaurantController();

        assertFalse(controller.canChangeStatus(OrderStatus.CREATED, OrderStatus.DELIVERED));
        assertFalse(controller.canChangeStatus(OrderStatus.ACCEPTED, OrderStatus.DELIVERED));
        assertFalse(controller.canChangeStatus(OrderStatus.DELIVERED, OrderStatus.CANCELED));
        assertFalse(controller.canChangeStatus(OrderStatus.CANCELED, OrderStatus.ACCEPTED));
    }
}
