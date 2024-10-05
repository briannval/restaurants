package com.brian.restaurants.assembler;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.brian.restaurants.controller.RestaurantController;
import com.brian.restaurants.model.Restaurant;

@Component
public class RestaurantModelAssembler implements RepresentationModelAssembler<Restaurant, EntityModel<Restaurant>> {

    @SuppressWarnings("null")
    @Override
    public EntityModel<Restaurant> toModel(Restaurant restaurant) {

        return EntityModel.of(restaurant,
                linkTo(methodOn(RestaurantController.class).one(restaurant.getId())).withSelfRel(),
                linkTo(methodOn(RestaurantController.class).all()).withRel("restaurants"));
    }
}
