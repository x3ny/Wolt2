package org.example.validation;

public class OrderValidator {
    public boolean isTotalPriceValid(double totalPrice){
        return totalPrice > 0;
    }
    public boolean isAddressValid(String address){
        if(address != null){
            if(address.length() >= 10){
                return true;
            }
        }
        return false;
    }
}
