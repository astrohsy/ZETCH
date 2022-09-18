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

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

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
    Restaurant getOneRestaurant(@PathVariable Long restaurantId) {
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
        List<User> restaurantOwner = verifyOwners(restaurantDto.getOwnerUsernames());
        Restaurant newRestaurant = new Restaurant(restaurantOwner, restaurantDto.getName(),
                restaurantDto.getCuisine(), restaurantDto.getAddress());
        restaurantRepository.save(newRestaurant);

        return "Restaurant saved";
    }

    /**
     * Verify and return Users corresponding to a list of usernames of restaurant owners.
     *
     * @param usernames List of usernames of owners
     * @return List of found Users
     * @throws NoSuchElementException If at least one User was not found
     */
    private List<User> verifyOwners(List<String> usernames) throws NoSuchElementException {
        if (usernames == null) {
            return null;
        }

        Set<String> usernamesSet = new HashSet<>(usernames);
        List<User> users = userRepository.findAllById(usernames);
        Set<String> foundUsernames = users.stream().map(User::getUsername).collect(Collectors.toSet());

        // Remove found usernames from provided usernames to check whether all were found
        usernamesSet.removeAll(foundUsernames);

        if (usernamesSet.isEmpty()) {
            return users;
        } else {
            throw new NoSuchElementException("User(s) not found: " + usernamesSet);
        }
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
