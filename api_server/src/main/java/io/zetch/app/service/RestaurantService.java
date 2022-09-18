package io.zetch.app.service;

import io.zetch.app.domain.Restaurant;
import io.zetch.app.domain.User;
import io.zetch.app.repo.RestaurantRepository;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Restaurant business logic
 */
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final UserRepository userRepository;

    @Autowired
    public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository) {
        this.restaurantRepository = restaurantRepository;
        this.userRepository = userRepository;
    }

    /**
     * Retrieve all restaurants
     *
     * @return List of all restaurants
     */
    public List<Restaurant> getAll() {
        return restaurantRepository.findAll();
    }

    /**
     * Retrieve one restaurant
     *
     * @param id Restaurant id
     * @return Restaurant
     */
    public Restaurant getOne(Long id) {
        return restaurantRepository.findById(id).orElseThrow(
                () -> new NoSuchElementException("Restaurant does not exist: " + id));
    }

    /**
     * Create a new Restaurant in the database
     *
     * @param ownerUsernames List of usernames of owners
     * @param name Restaurant name
     * @param cuisine Restaurant cuisine
     * @param address Restaurant address
     */
    public void createNew(List<String> ownerUsernames, String name, String cuisine, String address ) {
        List<User> restaurantOwner = verifyOwners(ownerUsernames);
        Restaurant newRestaurant = new Restaurant(restaurantOwner, name, cuisine, address);
        restaurantRepository.save(newRestaurant);
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
}
