package io.zetch.app.seed;

import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/** Component for loading sample data at runtime. */
@Component
public class DataLoader implements CommandLineRunner {

  @Autowired UserRepository userRepository;
  @Autowired LocationRepository locationRepository;

  @Override
  public void run(String... args) throws Exception {
    seedUserData();
    seedLocationData();
  }

  private void seedUserData() {
    if (userRepository.count() == 0) {
      userRepository.saveAll(SeedUsers.USERS);
    }
  }

  private void seedLocationData() {
    if (locationRepository.count() == 0) {
      locationRepository.saveAll(SeedLocations.LOCATIONS);
    }
  }
}
