package com.brian.restaurants.controller;

import com.brian.restaurants.exception.RestaurantNotFoundException;
import com.brian.restaurants.model.Restaurant;
import com.brian.restaurants.repository.RestaurantRepository;
import org.springframework.hateoas.EntityModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
public class RestaurantController {

    private final RestaurantRepository repository;

    RestaurantController(RestaurantRepository repository) {
        this.repository = repository;
    }

    // mapping is what type of HTTP request it is
    @GetMapping("/restaurants")
    List<Restaurant> all() {
        return repository.findAll();
    }

    @PostMapping("/restaurants")
    Restaurant newRestaurant(@RequestBody Restaurant newRestaurant) {
        return repository.save(newRestaurant);
    }

    @GetMapping("/restaurants/{id}")
    EntityModel<Restaurant> one(@PathVariable Long id) {
        /*
        OLD IMPLEMENTATION: not RESTful!
        return repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));
        */

        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        return EntityModel.of(restaurant,
                linkTo(methodOn(RestaurantController.class).one(id)).withSelfRel(),
                linkTo(methodOn(RestaurantController.class).all()).withRel("restaurants"));
    }

    @PutMapping("/restaurants/{id}")
    Restaurant replaceRestaurant(@RequestBody Restaurant newRestaurant, @PathVariable Long id) {
        return repository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(newRestaurant.getName());
                    restaurant.setAddress(newRestaurant.getAddress());
                    restaurant.setLatitude(newRestaurant.getLatitude());
                    restaurant.setLongitude(newRestaurant.getLongitude());
                    restaurant.setRating(newRestaurant.getRating());
                    restaurant.setGooglePlaceId(newRestaurant.getGooglePlaceId());
                    return repository.save(restaurant);
                })
                .orElseGet(() -> {
                    return repository.save(newRestaurant);
                });
    }

    @DeleteMapping("/restaurants/{id}")
    void deleteRestaurant(@PathVariable Long id) {
        repository.deleteById(id);
    }


}
