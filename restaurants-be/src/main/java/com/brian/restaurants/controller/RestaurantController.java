package com.brian.restaurants.controller;

import com.brian.restaurants.assembler.RestaurantModelAssembler;
import com.brian.restaurants.dto.LocationRequest;
import com.brian.restaurants.dto.RestaurantRequest;
import com.brian.restaurants.exception.RestaurantNotFoundException;
import com.brian.restaurants.model.Restaurant;
import com.brian.restaurants.repository.RestaurantRepository;
import com.brian.restaurants.service.GooglePlacesService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;

@RestController
public class RestaurantController {

    private final RestaurantRepository repository;

    private final GooglePlacesService gService;

    @SuppressWarnings("unused")
    private final RestaurantModelAssembler assembler;

    RestaurantController(RestaurantRepository repository, RestaurantModelAssembler assembler, GooglePlacesService gService) {
        this.repository = repository;
        this.assembler = assembler;
        this.gService = gService;
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
    Restaurant replaceRestaurant(@RequestBody RestaurantRequest r, @PathVariable Long id) {
        Restaurant replaceR = repository.findById(id)
                .orElseThrow(() -> new RestaurantNotFoundException(id));

        replaceR.setName(r.getName());
        replaceR.setAddress(r.getAddress());
        replaceR.setGooglePlaceId(r.getGooglePlaceId());
        replaceR.setLatitude(r.getLatitude());
        replaceR.setLongitude(r.getLongitude());

        return repository.save(replaceR);
    }

    @DeleteMapping("/restaurants/{id}")
    public String deleteRestaurant(@PathVariable Long id) {
        repository.deleteById(id);
        return "Successfully deleted restaurant with id " + id;
    }

    @PostMapping("/restaurants/recommend")
    List<RestaurantRequest> recommendRestaurants(@Valid @RequestBody LocationRequest locationRequest) {
        double latitude = locationRequest.getLatitude();
        double longitude = locationRequest.getLongitude();

        String gPlaceRes = gService.findNearbyRestaurants(latitude, longitude);

        List<RestaurantRequest> res = gService.parseGooglePlacesResponse(gPlaceRes);

        return res;
    }
}
