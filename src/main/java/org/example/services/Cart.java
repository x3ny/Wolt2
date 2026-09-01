package org.example.services;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.example.Classes.CartItem;
import org.example.Classes.MenuItem;

/** Holds the items selected for one order before checkout. */
public class Cart {
    private final ObservableList<CartItem> items = FXCollections.observableArrayList();

    public void add(MenuItem menuItem, int quantity) {
        if (menuItem == null) {
            throw new IllegalArgumentException("Menu item cannot be null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than 0");
        }

        for (CartItem item : items) {

            MenuItem existingMenuItem = item.getMenuItem();

            boolean sameMenuItem = existingMenuItem.equals(menuItem) || (existingMenuItem.getId() > 0 && existingMenuItem.getId() == menuItem.getId());

            if (sameMenuItem) {
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }

        items.add(new CartItem(menuItem, quantity));
    }

    public void decrease(MenuItem menuItem){
        for(CartItem item : items){
            if(item.getQuantity() > 1){
                item.setQuantity(item.getQuantity() - 1);
            }
            else{
                items.remove(item);
            }
            return;
        }
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public double getTotal() {
        return items.stream().mapToDouble(CartItem::getLineTotal).sum();
    }

    public ObservableList<CartItem> getItems() {
        return items;
    }

    public void clear() {
        items.clear();
    }
}
