package io.zetch.app.service;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.RestaurantRepository;
import io.zetch.app.repo.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Restaurant business logic */
@Service
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;
  private final UserRepository userRepository;

  @Autowired
  public RestaurantService(
      RestaurantRepository restaurantRepository, UserRepository userRepository) {
    this.restaurantRepository = restaurantRepository;
    this.userRepository = userRepository;
  }

  /**
   * Retrieve all restaurants
   *
   * @return List of all restaurants
   */
  public List<RestaurantEntity> getAll() {
    return restaurantRepository.findAll();
  }

  /**
   * Retrieve one restaurant
   *
   * @param name Restaurant name
   * @return Restaurant
   * @throws NoSuchElementException If Restaurant not found
   */
  public RestaurantEntity getOne(String name) throws NoSuchElementException {
    return verifyRestaurant(name);
  }

  /**
   * Update existing Restaurant with any non-null attributes. Changing username is not supported.
   *
   * @param name Name of Restaurant to be updated
   * @param newName New name
   * @param newCuisine New cuisine
   * @param newAddress New address
   * @return Updated User object
   * @throws NoSuchElementException If User not found
   */
  public RestaurantEntity update(String name, String newName, String newCuisine, String newAddress)
      throws IllegalArgumentException, NoSuchElementException {
    RestaurantEntity currRestaurant = verifyRestaurant(name);
    if (name != newName && restaurantRepository.existsByName(newName)) {
      throw new IllegalArgumentException("Name unavailable: " + newName);
    }

    currRestaurant.setName(newName);
    currRestaurant.setCuisine(newCuisine);
    currRestaurant.setAddress(newAddress);

    return restaurantRepository.save(currRestaurant);
  }

  /**
   * Assign owner to restaurant
   *
   * @param name Restaurant name
   * @param owner Owner name
   */
  public RestaurantEntity assignOwner(String name, String owner) throws NoSuchElementException {
    RestaurantEntity restaurant = verifyRestaurant(name);
    UserEntity user =
        userRepository
            .findByUsername(owner)
            .orElseThrow(() -> new NoSuchElementException("User does not exist: " + owner));

    List<UserEntity> ownerList = restaurant.getOwners();
    ownerList.add(user);
    restaurant.setOwners(ownerList);

    List<RestaurantEntity> restaurantList = user.getOwnedRestaurants();
    restaurantList.add(restaurant);
    user.setOwnedRestaurants(restaurantList);

    userRepository.save(user);
    return restaurantRepository.save(restaurant);
  }

  /**
   * Create a new Restaurant in the database
   *
   * @param name Restaurant name
   * @param cuisine Restaurant cuisine
   * @param address Restaurant address
   * @throws IllegalArgumentException If username unavailable or invalid Affiliation passed
   */
  public RestaurantEntity createNew(String name, String cuisine, String address)
      throws IllegalArgumentException {
    if (restaurantRepository.existsByName(name)) {
      throw new IllegalArgumentException("Name unavailable: " + name);
    }
    RestaurantEntity newRestaurant =
        RestaurantEntity.builder()
            .name(name)
            .cuisine(cuisine)
            .address(address)
            .owners(new ArrayList<>())
            .build();
    return restaurantRepository.save(newRestaurant);
  }

  /**
   * Verify and return Restaurant for a particular id
   *
   * @param name Restaurant name
   * @return Found Restaurant
   * @throws NoSuchElementException If Restaurant not found
   */
  public RestaurantEntity verifyRestaurant(String name) throws NoSuchElementException {
    return restaurantRepository
        .findByName(name)
        .orElseThrow(() -> new NoSuchElementException("Restaurant does not exist: " + name));
  }
}
