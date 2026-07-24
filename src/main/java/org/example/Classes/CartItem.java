package org.example.Classes;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class CartItem {
   private MenuItem menuItem;
   private int quantity;

   public CartItem(MenuItem menuItem, int quantity){
       this.menuItem = menuItem;
       this.quantity = quantity;
   }

   public double getLineTotal(){
       return menuItem.getPrice() * quantity;
   }

   public String getMenuItemName(){
       return menuItem.getName();
   }

   public double getUnitPrice(){
       return menuItem.getPrice();
   }


}
