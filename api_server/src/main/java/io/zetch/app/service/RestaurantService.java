package io.zetch.app.service;

import io.zetch.app.domain.Restaurant;
import io.zetch.app.repo.RestaurantRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Restaurant business logic */
@Service
public class RestaurantService {
  private final RestaurantRepository restaurantRepository;

  @Autowired
  public RestaurantService(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
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
   * @throws NoSuchElementException If Restaurant not found
   */
  public Restaurant getOne(Long id) throws NoSuchElementException {
    return verifyRestaurant(id);
  }

  /**
   * Update existing Restaurant with any non-null attributes. Changing username is not supported.
   *
   * @param restaurantId Username of User to be updated
   * @param newName New name
   * @param newCuisine New cuisine
   * @param newAddress New address
   * @return Updated User object
   * @throws NoSuchElementException If User not found
   */
  public Restaurant update(long restaurantId, String newName, String newCuisine, String newAddress)
          throws NoSuchElementException {
    Restaurant currRestaurant = verifyRestaurant(restaurantId);

    // TODO: Maybe there is a better way to set
    if (newName != null) {
      currRestaurant.setName(newName);
    }
    if (newCuisine != null) {
      currRestaurant.setCuisine(newCuisine);
    }
    if (newAddress != null) {
      currRestaurant.setAddress(newAddress);
    }

    return restaurantRepository.save(currRestaurant);
  }

  /**
   * Create a new Restaurant in the database
   *
   * @param name Restaurant name
   * @param cuisine Restaurant cuisine
   * @param address Restaurant address
   */
  public Restaurant createNew(String name, String cuisine, String address) {
    Restaurant newRestaurant = new Restaurant(name, cuisine, address);
    return restaurantRepository.save(newRestaurant);
  }

  /**
   * Verify and return Restaurant for a particular id
   *
   * @param id Restaurant Id
   * @return Found Restaurant
   * @throws NoSuchElementException If Restaurant not found
   */
  public Restaurant verifyRestaurant(Long id) throws NoSuchElementException {
    return restaurantRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Restaurant does not exist: " + id));
  }
}
