package com.brian.restaurants.service;

import com.brian.restaurants.dto.RestaurantRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class GooglePlacesService {

    @Value("${google.places.api.key}")
    private String googlePlacesApiKey;

    private final int GOOGLE_PLACES_RADIUS = 1500;
    private final String GOOGLE_PLACES_TYPE = "restaurant";

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    public GooglePlacesService(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
    }

    public String findNearbyRestaurants(double latitude, double longitude) {
        String url = UriComponentsBuilder.fromHttpUrl("https://maps.googleapis.com/maps/api/place/nearbysearch/json")
                .queryParam("location", latitude + "," + longitude)
                .queryParam("radius", this.GOOGLE_PLACES_RADIUS)
                .queryParam("type", this.GOOGLE_PLACES_TYPE)
                .queryParam("key", this.googlePlacesApiKey)
                .toUriString();

        return restTemplate.getForObject(url, String.class);
    }

    public List<RestaurantRequest> parseGooglePlacesResponse(String jsonResponse) {
        List<RestaurantRequest> restaurants = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(jsonResponse);
            JsonNode results = root.path("results");

            for (JsonNode result : results) {
                RestaurantRequest restaurant = new RestaurantRequest();

                restaurant.setName(result.path("name").asText());
                restaurant.setAddress(result.path("vicinity").asText());
                restaurant.setGooglePlaceId(result.path("place_id").asText());

                JsonNode location = result.path("geometry").path("location");
                restaurant.setLatitude(location.path("lat").asDouble());
                restaurant.setLongitude(location.path("lng").asDouble());

                restaurants.add(restaurant);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return restaurants;
    }
}

