package com.brian.restaurants.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "restaurant")
@EqualsAndHashCode
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    private String name;
    private String address;
    private String googlePlaceId;
    private Double latitude;
    private Double longitude;

    @OneToMany(mappedBy = "restaurant", cascade = CascadeType.ALL)
    @JsonManagedReference // avoid circular reference and recursion
    private Set<Review> reviews;

    public void addReview(Review review) {
       this.reviews.add(review);
    }
}
