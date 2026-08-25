package org.example.web;

import org.example.Classes.MenuItem;
import org.springframework.ui.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.Classes.Restaurant;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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
    public String restaurantMenu(@PathVariable("id") int id,Model model) {
        Restaurant restaurant = entityManager.find(Restaurant.class, id);
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

        return "restaurant-menu";
    }
}
