package io.zetch.app.service;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Location business logic. */
@Service
public class LocationService {
  private final LocationRepository locationRepository;
  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;

  /** LocationService constructor. */
  @Autowired
  public LocationService(
      LocationRepository locationRepository,
      UserRepository userRepository,
      ReviewRepository reviewRepository) {
    this.locationRepository = locationRepository;
    this.userRepository = userRepository;
    this.reviewRepository = reviewRepository;
  }

  /**
   * Retrieve all location.
   *
   * @return List of all locations
   */
  public List<LocationEntity> getAll() {
    return locationRepository.findAll();
  }

  /**
   * Retrieve one location.
   *
   * @param name Location name
   * @return Location
   * @throws NoSuchElementException If Location not found
   */
  public LocationEntity getOne(String name) throws NoSuchElementException {
    return verifyLocation(name);
  }

  /**
   * Search location by name & type.
   *
   * @param name Location name
   * @param type Location type
   * @return List of locations
   */
  public List<LocationEntity> search(String name, String description, String type) {
    Type searchType;

    if (type == null) {
      searchType = null;
    } else {
      searchType = Type.fromString(type);
    }

    return locationRepository.search(name, description, searchType);
  }

  /**
   * Update existing Location with any non-null attributes. Changing username is not supported.
   *
   * @param name Name of Location to be updated
   * @param newName New name
   * @param newDescription New description
   * @param newAddress New address
   * @param type New type
   * @return Updated User object
   * @throws NoSuchElementException If User not found
   */
  public LocationEntity update(
      String name, String newName, String newDescription, String newAddress, String type)
      throws IllegalArgumentException, NoSuchElementException {
    LocationEntity currLocation = verifyLocation(name);

    if (newName != null) {
      if (!name.equals(newName) && locationRepository.existsByName(newName)) {
        throw new IllegalArgumentException("Name unavailable: " + newName);
      }
      currLocation.setName(newName);
    }

    if (newDescription != null) {
      currLocation.setDescription(newDescription);
    }

    if (newAddress != null) {
      currLocation.setAddress(newAddress);
    }

    if (type != null) {
      currLocation.setType(Type.fromString(type));
    }

    return locationRepository.save(currLocation);
  }

  /**
   * Assign owner to location.
   *
   * @param name Location name
   * @param owner Owner name
   */
  public LocationEntity assignOwner(String name, String owner) throws NoSuchElementException {
    LocationEntity location = verifyLocation(name);
    UserEntity user =
        userRepository
            .findByUsernameIgnoreCase(owner)
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
   * Returns Location's rating histogram.
   *
   * @param name Name of Location
   * @return Location's rating histogram
   * @throws NoSuchElementException If Location not found
   */
  public Map<String, String> getRatingHistogram(String name) throws NoSuchElementException {
    verifyLocation(name);

    Map<String, String> histogram = new HashMap<>();
    histogram.put(
        "1", Long.toString(reviewRepository.countByLocation_NameIgnoreCaseAndRating(name, 1)));
    histogram.put(
        "2", Long.toString(reviewRepository.countByLocation_NameIgnoreCaseAndRating(name, 2)));
    histogram.put(
        "3", Long.toString(reviewRepository.countByLocation_NameIgnoreCaseAndRating(name, 3)));
    histogram.put(
        "4", Long.toString(reviewRepository.countByLocation_NameIgnoreCaseAndRating(name, 4)));
    histogram.put(
        "5", Long.toString(reviewRepository.countByLocation_NameIgnoreCaseAndRating(name, 5)));

    return histogram;
  }

  /**
   * Create a new Location in the database.
   *
   * @param name Location name
   * @param description Location description
   * @param address Location address
   * @param type Location type
   * @throws IllegalArgumentException If username unavailable or invalid Type passed
   */
  public LocationEntity createNew(String name, String description, String address, String type)
      throws IllegalArgumentException {
    if (locationRepository.existsByName(name)) {
      throw new IllegalArgumentException("Name unavailable: " + name);
    }
    LocationEntity newLocation =
        LocationEntity.builder()
            .name(name)
            .description(description)
            .address(address)
            .owners(new ArrayList<>())
            .type(Type.fromString(type))
            .build();
    return locationRepository.save(newLocation);
  }

  /**
   * Deletes a Location from the database.
   *
   * @param name Name of Location to delete
   * @return Location that was just deleted
   * @throws NoSuchElementException If Location not found
   */
  public LocationEntity delete(String name) throws NoSuchElementException {
    LocationEntity location = verifyLocation(name);
    locationRepository.delete(location);
    return location;
  }

  /**
   * Returns the average rating of a Location. Returns zero if Location has no ratings.
   *
   * @param name Name of Location
   * @return Average rating
   * @throws NoSuchElementException If Location not found
   */
  public double averageRating(String name) throws NoSuchElementException {
    verifyLocation(name);
    List<Integer> ratings =
        reviewRepository.findByLocation_NameIgnoreCase(name).stream()
            .map(ReviewEntity::getRating)
            .toList();

    double sum = ratings.stream().reduce(0, Integer::sum);

    return sum == 0 ? 0 : sum / ratings.size();
  }

  /**
   * Verify and return Location for a particular id.
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
