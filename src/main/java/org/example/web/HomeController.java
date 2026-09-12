package org.example.web;

import jakarta.servlet.http.HttpSession;
import jakarta.transaction.Transactional;
import org.example.Classes.*;
import org.example.services.Cart;
import org.example.services.OrderFactory;
import org.springframework.ui.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;


@Controller
public class HomeController {
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/")
    public String homePage(Model model) {
        List<Restaurant> restaurants = entityManager.createQuery(
                "SELECT restaurant FROM Restaurant restaurant " +
                        "ORDER BY restaurant.restaurantName",
                Restaurant.class
        ).getResultList();

        model.addAttribute("restaurants", restaurants);

        return "home";
    }

    @GetMapping("/restaurants/{id}")
    public String restaurantMenu(@PathVariable("id") int id,Model model, HttpSession session) {
        Restaurant restaurant = entityManager.find(Restaurant.class, id);

        if(restaurant == null) {
            return "redirect:/";
        }

        List<MenuItem> menuItems = entityManager.createQuery(
                        "SELECT menuItem FROM MenuItem menuItem " +
                                "WHERE menuItem.restaurantId = :restaurantId " +
                                "AND menuItem.available = true " +
                                "ORDER BY menuItem.name",
                        MenuItem.class
                )
                .setParameter("restaurantId", id)
                .getResultList();

        model.addAttribute("restaurant", restaurant);
        model.addAttribute("menuItems", menuItems);


        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("cart", cart);

        if(cart != null && !cart.isEmpty()) {
            int currentRestaurantId = cart.getItems().getFirst().getMenuItem().getRestaurantId();

            if(currentRestaurantId != id){
                cart.clear();
            }
        }


        return "restaurant-menu";
    }

    @PostMapping("/cart/add")
    public String addCart(@RequestParam("menuItemId") int menuItemId, @RequestParam("quantity") int quantity, HttpSession session ) {

        Cart cart = (Cart) session.getAttribute("cart");

        MenuItem menuItem = entityManager.find(MenuItem.class, menuItemId);

        if(menuItem == null || !menuItem.isAvailable() || quantity <=0){
            return "redirect:/";
        }

        if(cart == null){
            cart = new Cart();
            session.setAttribute("cart", cart);
        }


        if(!cart.isEmpty()){
            int currentRestaurantId = cart.getItems().get(0).getMenuItem().getRestaurantId();

            if(currentRestaurantId != menuItem.getRestaurantId()){
                cart.clear();
            }
        }

        cart.add(menuItem, quantity);



        return "redirect:/restaurants/" + menuItem.getRestaurantId();
    }

    @PostMapping("/cart/decrease")
    public String decreaseCart(@RequestParam("menuItemId") int menuItemId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");
        MenuItem menuItem = entityManager.find(MenuItem.class, menuItemId);
        if(menuItem == null || cart == null || cart.isEmpty()){
            return "redirect:/";
        }
        cart.decreaseQuantity(menuItem);

        return "redirect:/restaurants/" + menuItem.getRestaurantId();
    }

    @PostMapping("/cart/clear")
    public String clearCart(@RequestParam("restaurantId") int restaurantId, HttpSession session) {
        Cart cart = (Cart) session.getAttribute("cart");

        if(cart != null){
            cart.clear();
        }

        return "redirect:/restaurants/" + restaurantId;
    }

    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        if(cart == null || cart.isEmpty()){
            return "redirect:/";
        }

        model.addAttribute("cart", cart);
        return "checkout";
    }


    @Transactional
    @PostMapping("/checkout/place-order")
    public String placeOrder(@RequestParam("deliveryAddress") String deliveryAddress, @RequestParam("paymentMethod") String paymentMethod, HttpSession session, Model model) {
        Cart cart = (Cart) session.getAttribute("cart");

        if(cart == null || cart.isEmpty()){
            return "redirect:/";
        }

        String trimmedDeliveryAddress = deliveryAddress.trim();
        String paymentMethodName = paymentMethod.trim();

        if(trimmedDeliveryAddress.isBlank()){
            model.addAttribute("cart",cart);
            model.addAttribute("errorDeliveryAddress", "Delivery address is required.");
            return "checkout";
        }

        if(paymentMethodName.isBlank()){
           model.addAttribute("cart",cart);
           model.addAttribute("errorPaymentMethod", "Payment method is required.");
           return "checkout";
        }

        int restaurantId = cart.getItems().getFirst().getMenuItem().getRestaurantId();

        int customerId = 3;
        int driverId = 0;
        boolean paid = paymentMethodName.equals("CARD");

        OrderFactory orderFactory = new OrderFactory();

        FoodOrder foodOrder = orderFactory.createFoodOrder(
                customerId,
                driverId,
                restaurantId,
                cart.getTotal(),
                trimmedDeliveryAddress,
                paymentMethodName,
                paid
        );

        entityManager.persist(foodOrder);
        entityManager.flush();

        for(CartItem cartItem : cart.getItems()){
            OrderItem orderItem = new OrderItem(
                    foodOrder.getId(),
                    cartItem.getMenuItem().getId(),
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice()
            );

            entityManager.persist(orderItem);
        }

        cart.clear();



        return "redirect:/orders/" + foodOrder.getId();
    }

    @GetMapping("/orders/{id}")
    public String ordersPage(@PathVariable("id") int id, Model model) {
        FoodOrder foodOrder = entityManager.find(FoodOrder.class, id);

        if(foodOrder == null){
            return "redirect:/";
        }

        List<OrderItem> orderItems = entityManager.createQuery(
                "SELECT orderItem FROM OrderItem orderItem " +
                        "WHERE orderItem.foodOrderId = :foodOrderId ",
                OrderItem.class
        ).setParameter("foodOrderId", id).getResultList();

        for(OrderItem orderItem : orderItems){
            MenuItem menuItem = entityManager.find(MenuItem.class, orderItem.getMenuItemId());

            if(menuItem != null){
                orderItem.setMenuItemName(menuItem.getName());
            }
        }

        model.addAttribute("foodOrder", foodOrder);
        model.addAttribute("orderItems", orderItems);





        return "order";
    }


}
