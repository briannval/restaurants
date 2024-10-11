package com.brian.restaurants.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Set;

@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String googlePlaceId;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    private Set<Review> reviews;

    public void addReview(Review review) {
       this.reviews.add(review);
    }
}
