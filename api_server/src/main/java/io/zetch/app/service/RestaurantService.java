package io.zetch.app.service;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.repo.RestaurantRepository;
import java.util.ArrayList;
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
  public List<RestaurantEntity> getAll() {
    return restaurantRepository.findAll();
  }

  /**
   * Retrieve one restaurant
   *
   * @param id Restaurant id
   * @return Restaurant
   * @throws NoSuchElementException If Restaurant not found
   */
  public RestaurantEntity getOne(Long id) throws NoSuchElementException {
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
  public RestaurantEntity update(long restaurantId, String newName, String newCuisine, String newAddress)
          throws NoSuchElementException {
    RestaurantEntity currRestaurant = verifyRestaurant(restaurantId);

    // TODO: Maybe there is a better way to set
    currRestaurant.setName(newName);
    currRestaurant.setCuisine(newCuisine);
    currRestaurant.setAddress(newAddress);

    return restaurantRepository.save(currRestaurant);
  }

  /**
   * Create a new Restaurant in the database
   *
   * @param name Restaurant name
   * @param cuisine Restaurant cuisine
   * @param address Restaurant address
   */
  public RestaurantEntity createNew(String name, String cuisine, String address) {
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
   * @param id Restaurant Id
   * @return Found Restaurant
   * @throws NoSuchElementException If Restaurant not found
   */
  public RestaurantEntity verifyRestaurant(Long id) throws NoSuchElementException {
    return restaurantRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Restaurant does not exist: " + id));
  }
}
