package com.brian.restaurants.config;

import com.brian.restaurants.model.Restaurant;
import com.brian.restaurants.repository.RestaurantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(RestaurantRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new Restaurant(
                    null,
                    "Koerner's Pub",
                    "1758 West Mall, Vancouver",
                    "ChIJzbpt5LNyhlQRuvflSCUx9ns",
                    49.2684653,
                    -123.2581069,
                    4.1
            )));

            log.info("Preloading " + repository.save(new Restaurant(
                    null,
                    "Tim Hortons",
                    "David Lam, 2015 Main Mall, Vancouver",
                    "ChIJaVgq5MtyhlQRhGixrme65Qg",
                    49.2671894302915,
                    -123.2531234697085,
                    3.1
            )));

            log.info("Preloading " + repository.save(new Restaurant(
                    null,
                    "The Mighty Oak @ the Well Cafe",
                    "5800 University Boulevard, Vancouver",
                    "ChIJt3eRBshyhlQRNjbZO0iuprw",
                    49.265648,
                    -123.244093,
                    4.8
            )));
        };
    }
}
