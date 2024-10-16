package com.brian.restaurants;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class RestaurantsApplication {
	public static void main(String[] args) {
        SpringApplication application = new SpringApplication(RestaurantsApplication.class);
        application.run(args);
    }

    @GetMapping("/")
    public String welcome(@RequestParam(value = "name", defaultValue = "World") String name) {
        // test route
        return String.format("Hello %s! Welcome to the Restaurants API!", name);
    }
}
