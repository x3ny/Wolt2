package org.example.javafx;


import org.example.validation.OrderValidator;
import org.junit.jupiter.api.Test;
import org.example.javafx.RestaurantController;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OrderValidatorTest {
    @Test
    void positivePriceIsValid(){
        OrderValidator validator = new OrderValidator();
        boolean result = validator.isTotalPriceValid(25.50);

        assertTrue(result);
    }

    @Test
    void negativePriceIsInvalid(){
        OrderValidator validator = new OrderValidator();
        boolean result = validator.isTotalPriceValid(0);

        assertFalse(result);
    }

    @Test
    void negativePriceIsInvalid2(){
        OrderValidator validator = new OrderValidator();
        boolean result = validator.isTotalPriceValid(-1);
        assertFalse(result);
    }

    @Test
    void shortAddressIsInvalid(){
        OrderValidator validator = new OrderValidator();
        boolean result = validator.isAddressValid("aaakjjhjh");
        assertFalse(result);
    }

    @Test
    void tenCharacterAddressIsValid(){
        OrderValidator validator = new OrderValidator();
        boolean result = validator.isAddressValid("aaakjjhjhs");
        assertTrue(result);
    }

    @Test
    void nullAddressIsInvalid(){
        OrderValidator validator = new OrderValidator();
        boolean result = validator.isAddressValid(null);
        assertFalse(result);
    }
}
