package com.brian.restaurants.controller;

import com.brian.restaurants.exception.RestaurantNotFoundException;
import com.brian.restaurants.model.Restaurant;
import com.brian.restaurants.model.Review;
import com.brian.restaurants.repository.RestaurantRepository;
import com.brian.restaurants.repository.ReviewRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewController {

    private final ReviewRepository repository;
    private final RestaurantRepository restaurantRepository;

    ReviewController(ReviewRepository repository, RestaurantRepository restaurantRepository) {
        this.repository = repository;
        this.restaurantRepository = restaurantRepository;
    }

    @GetMapping("/reviews")
    public List<Review> all() { return repository.findAll(); }

    @PostMapping("/{restaurantId}/reviews")
    Review newReview(@PathVariable Long restaurantId, @RequestBody Review newReview) {
        Restaurant r = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new RestaurantNotFoundException(restaurantId));

        newReview.setRestaurant(r);

        r.addReview(newReview);

        repository.save(newReview);

        restaurantRepository.save(r);

        return newReview;
    }
}
