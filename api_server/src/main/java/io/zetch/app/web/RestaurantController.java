package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.Restaurant;
import io.zetch.app.domain.RestaurantDto;
import io.zetch.app.domain.User;
import io.zetch.app.repo.RestaurantRepository;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/restaurants")
@Tag(name = "Restaurants")
public class RestaurantController {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Autowired
    public RestaurantController(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * @return A list of all restraurants
     */
    @GetMapping(path="/")
    @Operation(summary = "Retrieve all restaurants")
    @ResponseBody Iterable<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    /**
     * @param restaurantId Restaurant's id
     * @return A restaurant by id
     */
    @GetMapping("/{restaurantId}")
    @Operation(summary = "Retrieve a single restaurant")
    Restaurant getOneRestaurant(@PathVariable Integer restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new NoSuchElementException("Restaurant does not exist: " + restaurantId));
    }

    /**
     * Create a restaurant
     * @param restaurantDto Restaurant data transfer object
     * @return Confirmation message if successful
     */
    @PostMapping(path="/")
    @Operation(summary = "Create a new restaurant")
    @ResponseBody String addNewRestaurant(@RequestBody @Validated RestaurantDto restaurantDto) {
        User restaurantOwner = verifyOwner(restaurantDto.getOwnerUsername());
        Restaurant newRestaurant = new Restaurant(restaurantOwner, restaurantDto.getName(),
                restaurantDto.getCuisine(), restaurantDto.getAddress());
        restaurantRepository.save(newRestaurant);

        return "Restaurant saved";
    }

    /**
     * Verify and return User corresponding to restaurant's owner.
     *
     * @param username Owner's username
     * @return the found User
     * @throws NoSuchElementException if no User found
     */
    private User verifyOwner(String username) throws NoSuchElementException {
        if (username == null) {
            return null;
        }

        return userRepository.findById(username).orElseThrow(() ->
                new NoSuchElementException("User does not exist: " + username));
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
