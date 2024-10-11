package com.brian.restaurants.repository;

import com.brian.restaurants.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
