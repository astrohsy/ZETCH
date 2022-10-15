package io.zetch.app.seed;

import io.zetch.app.repo.RestaurantRepository;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

  @Autowired UserRepository userRepository;
  @Autowired RestaurantRepository restaurantRepository;

  @Override
  public void run(String... args) throws Exception {
    seedUserData();
    seedRestaurantData();
  }

  private void seedUserData() {
    if (userRepository.count() == 0) {
      userRepository.saveAll(SeedUsers.USERS);
    }
  }

  private void seedRestaurantData() {
    if (restaurantRepository.count() == 0) {
      restaurantRepository.saveAll(SeedRestaurants.RESTAURANTS);
    }
  }
}
