package org.example.web;

import jakarta.servlet.http.HttpSession;
import org.example.Classes.MenuItem;
import org.example.services.Cart;
import org.springframework.ui.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.Classes.Restaurant;
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

        //
        Cart cart = (Cart) session.getAttribute("cart");
        model.addAttribute("cart", cart);

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
}
