package org.example.services;

import org.example.Classes.MenuItem;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CartTest {
    @Test
    void addingSameMenuItemIncreasesItsQuantity() {
        Cart cart = new Cart();
        MenuItem pizza = new MenuItem(1, "Pizza", "", 8.50, true);

        cart.add(pizza, 2);
        cart.add(pizza, 3);

        assertEquals(1, cart.getItems().size());
        assertEquals(5, cart.getItems().getFirst().getQuantity());
        assertEquals(42.50, cart.getTotal(), 0.001);
    }

    @Test
    void rejectsNonPositiveQuantity() {
        Cart cart = new Cart();
        MenuItem pizza = new MenuItem(1, "Pizza", "", 8.50, true);

        assertThrows(IllegalArgumentException.class, () -> cart.add(pizza, 0));
    }

    @Test
    void seperateObjectsWithSameIdAreCombined(){
        Cart cart = new Cart();
        MenuItem pizza = new MenuItem(1, "Pizza", "", 8.50, true);
        MenuItem pizza2 = new MenuItem(1, "Pizza", "", 8.50, true);

        pizza.setId(10);
        pizza2.setId(10);

        cart.add(pizza, 1);
        cart.add(pizza2, 2);

        assertEquals(1, cart.getItems().size());
        assertEquals(3, cart.getItems().getFirst().getQuantity());
    }

    @Test
    void decreasingQuantitySubtractsOne(){
        Cart cart = new Cart();
        MenuItem pizza = new MenuItem(1, "Pizza", "", 8.50, true);
        cart.add(pizza, 2);
        cart.decrease(pizza);

        assertEquals(1, cart.getItems().size());
    }

    @Test
    void decreasingLastQuantityRemovesItem(){
        Cart cart = new Cart();
        MenuItem pizza = new MenuItem(1, "Pizza", "", 8.50, true);
        cart.add(pizza, 1);
        cart.decrease(pizza);

        assertEquals(0, cart.getItems().size());

    }
}
