package com.brian.restaurants.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestaurantNotFoundAdvice {

    @ExceptionHandler(RestaurantNotFoundException.class) // handles only RestaurantNotFoundException
    @ResponseStatus(HttpStatus.NOT_FOUND) // returns HTTP 404 error
    String restaurantNotFoundHandler(RestaurantNotFoundException e) {
        return e.getMessage();
    }
}
