package org.example.javafx;

import org.example.Classes.CartItem;
import org.example.Classes.MenuItem;
import org.example.services.Cart;
import org.junit.jupiter.api.Test;

import org.example.Classes.OrderStatus;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void addingDifferentMenuItemsKeepsBothItems(){
        RestaurantController controller = new RestaurantController();
        MenuItem menuItem1 = new MenuItem(1,"Burger","Burger burger", 13.99,true);
        MenuItem menuItem2 = new MenuItem(1,"Pizza","Pizza pizza", 13.99,true);

        Cart cart = new Cart();
        cart.add(menuItem1,1);
        cart.add(menuItem2,1);

        assertEquals(2, cart.getItems().size());


    }
}
