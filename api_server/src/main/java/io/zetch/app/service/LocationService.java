package io.zetch.app.service;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Location business logic */
@Service
public class LocationService {
  private final LocationRepository locationRepository;
  private final UserRepository userRepository;

  @Autowired
  public LocationService(LocationRepository locationRepository, UserRepository userRepository) {
    this.locationRepository = locationRepository;
    this.userRepository = userRepository;
  }

  /**
   * Retrieve all location
   *
   * @return List of all locations
   */
  public List<LocationEntity> getAll() {
    return locationRepository.findAll();
  }

  /**
   * Retrieve one location
   *
   * @param name Location name
   * @return Location
   * @throws NoSuchElementException If Location not found
   */
  public LocationEntity getOne(String name) throws NoSuchElementException {
    return verifyLocation(name);
  }

  /**
   * Update existing Location with any non-null attributes. Changing username is not supported.
   *
   * @param name Name of Location to be updated
   * @param newName New name
   * @param newCuisine New cuisine
   * @param newAddress New address
   * @return Updated User object
   * @throws NoSuchElementException If User not found
   */
  public LocationEntity update(String name, String newName, String newCuisine, String newAddress)
      throws IllegalArgumentException, NoSuchElementException {
    LocationEntity currLocation = verifyLocation(name);

    if (newName != null) {
      if (!name.equals(newName) && locationRepository.existsByName(newName)) {
        throw new IllegalArgumentException("Name unavailable: " + newName);
      }
      currLocation.setName(newName);
    }

    if (newCuisine != null) {
      currLocation.setCuisine(newCuisine);
    }

    if (newAddress != null) {
      currLocation.setAddress(newAddress);
    }

    return locationRepository.save(currLocation);
  }

  /**
   * Assign owner to location
   *
   * @param name Location name
   * @param owner Owner name
   */
  public LocationEntity assignOwner(String name, String owner) throws NoSuchElementException {
    LocationEntity location = verifyLocation(name);
    UserEntity user =
        userRepository
            .findByUsername(owner)
            .orElseThrow(() -> new NoSuchElementException("User does not exist: " + owner));

    List<UserEntity> ownerList = location.getOwners();
    ownerList.add(user);
    location.setOwners(ownerList);

    List<LocationEntity> locationList = user.getOwnedLocations();
    locationList.add(location);
    user.setOwnedLocations(locationList);

    userRepository.save(user);
    return locationRepository.save(location);
  }

  /**
   * Create a new Location in the database
   *
   * @param name Location name
   * @param cuisine Location cuisine
   * @param address Location address
   * @throws IllegalArgumentException If username unavailable or invalid Affiliation passed
   */
  public LocationEntity createNew(String name, String cuisine, String address)
      throws IllegalArgumentException {
    if (locationRepository.existsByName(name)) {
      throw new IllegalArgumentException("Name unavailable: " + name);
    }
    LocationEntity newLocation =
        LocationEntity.builder()
            .name(name)
            .cuisine(cuisine)
            .address(address)
            .owners(new ArrayList<>())
            .build();
    return locationRepository.save(newLocation);
  }

  /**
   * Verify and return Location for a particular id
   *
   * @param name Location name
   * @return Found Location
   * @throws NoSuchElementException If Location not found
   */
  public LocationEntity verifyLocation(String name) throws NoSuchElementException {
    return locationRepository
        .findByName(name)
        .orElseThrow(() -> new NoSuchElementException("Location does not exist: " + name));
  }
}
