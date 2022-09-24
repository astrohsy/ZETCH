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
