package com.brian.restaurants.controller;

import com.brian.restaurants.assembler.RestaurantModelAssembler;
import com.brian.restaurants.exception.RestaurantNotFoundException;
import com.brian.restaurants.model.Restaurant;
import com.brian.restaurants.repository.RestaurantRepository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantController {

    private final RestaurantRepository repository;

    @SuppressWarnings("unused")
    private final RestaurantModelAssembler assembler;

    @Value("${google.places.api.key}")
    private String googlePlacesApiKey;

    RestaurantController(RestaurantRepository repository, RestaurantModelAssembler assembler) {
        this.repository = repository;
        this.assembler = assembler;
    }

    // mapping is what type of HTTP request it is
    @GetMapping("/restaurants")
    public List<Restaurant> all() {
        return repository.findAll();
    }

    @PostMapping("/restaurants")
    Restaurant newRestaurant(@RequestBody Restaurant newRestaurant) {
        return repository.save(newRestaurant);
    }

    @GetMapping("/restaurants/{id}")
    public Restaurant one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        /*
        Restaurant restaurant = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        return assembler.toModel(restaurant);
        */
    }

    @PutMapping("/restaurants/{id}")
    Restaurant replaceRestaurant(@RequestBody Restaurant newRestaurant, @PathVariable Long id) {
        return repository.findById(id)
                .map(restaurant -> {
                    restaurant.setName(newRestaurant.getName());
                    restaurant.setAddress(newRestaurant.getAddress());
                    restaurant.setLatitude(newRestaurant.getLatitude());
                    restaurant.setLongitude(newRestaurant.getLongitude());
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
