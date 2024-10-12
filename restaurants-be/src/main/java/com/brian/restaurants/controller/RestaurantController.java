package com.brian.restaurants.controller;

import com.brian.restaurants.assembler.RestaurantModelAssembler;
import com.brian.restaurants.dto.LocationRequest;
import com.brian.restaurants.dto.RestaurantRequest;
import com.brian.restaurants.exception.RestaurantNotFoundException;
import com.brian.restaurants.model.Restaurant;
import com.brian.restaurants.repository.RestaurantRepository;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
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
    public Restaurant newRestaurant(@RequestBody RestaurantRequest r) {
        Restaurant newR = new Restaurant(null,
                r.getName(),
                r.getAddress(),
                r.getGooglePlaceId(),
                r.getLatitude(),
                r.getLongitude(),
                new HashSet<>());
        return repository.save(newR);
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
    public String deleteRestaurant(@PathVariable Long id) {
        repository.deleteById(id);
        return "Successfully deleted restaurant with id " + id;
    }

    @PostMapping("/restaurants/recommend")
    String recommendRestaurants(@Valid @RequestBody LocationRequest locationRequest) {
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        return "Your latitude is " + latitude + " and longitude is " + longitude;
    }
}
