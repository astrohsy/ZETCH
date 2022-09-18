package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.Restaurant;
import io.zetch.app.domain.RestaurantDto;
import io.zetch.app.service.RestaurantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/restaurants")
@Tag(name = "Restaurants")
public class RestaurantController {
    private final RestaurantService restaurantService;

    @Autowired
    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * @return A list of all restraurants
     */
    @GetMapping(path="/")
    @Operation(summary = "Retrieve all restaurants")
    @ResponseBody Iterable<Restaurant> getAllRestaurants() {
        return restaurantService.getAll();
    }

    /**
     * @param restaurantId Restaurant's id
     * @return A restaurant by id
     */
    @GetMapping("/{restaurantId}")
    @Operation(summary = "Retrieve a single restaurant")
    Restaurant getOneRestaurant(@PathVariable Long restaurantId) {
        return restaurantService.getOne(restaurantId);
    }

    /**
     * @param restaurantDto Restaurant data transfer object
     * @return Confirmation message if successful
     */
    @PostMapping(path="/")
    @Operation(summary = "Create a new restaurant")
    @ResponseBody String addNewRestaurant(@RequestBody @Validated RestaurantDto restaurantDto) {
        restaurantService.createNew(restaurantDto.getOwnerUsernames(), restaurantDto.getName(),
                restaurantDto.getCuisine(), restaurantDto.getAddress());

        return "Restaurant saved";
    }

    /**
     * Exception handler if NoSuchElementException is thrown in this Controller
     * @param ex Exception
     * @return Error message string
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    String return404(NoSuchElementException ex) {
        return ex.getMessage();
    }
}
