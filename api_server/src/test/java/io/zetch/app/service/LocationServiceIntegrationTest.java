package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.hamcrest.core.Is.is;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class LocationServiceIntegrationTest {

  private static final String NAME_1 = "Bob's";
  private static final String NAME_2 = "Sally's";
  LocationEntity l1 =
      LocationEntity.builder()
          .owners(new ArrayList<>())
          .name(NAME_1)
          .description("Italian")
          .address("1234 Broadway")
          .type(Type.fromString("restaurant"))
          .build();
  LocationEntity l2 =
      LocationEntity.builder()
          .owners(new ArrayList<>())
          .name(NAME_2)
          .description("Italian")
          .address("1234 Broadway")
          .type(Type.fromString("restaurant"))
          .build();
  UserEntity u1 =
      UserEntity.builder()
          .username("cat")
          .displayName("Cat")
          .email("cat@cat.cat")
          .affiliation(Affiliation.OTHER)
          .ownedLocations(new ArrayList<>())
          .build();
  ReviewEntity r1 = ReviewEntity.builder().rating(3).user(u1).location(l1).build();
  @Autowired private LocationService locationService;
  @Autowired private LocationRepository locationRepository;
  @Autowired private UserRepository userRepository;
  @Autowired private ReviewRepository reviewRepository;

  @AfterEach
  void resetDatabase() {
    reviewRepository.deleteAll();
    locationRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  void getAll() {
    locationRepository.save(l1);
    locationRepository.save(l2);
    List<LocationEntity> locations = locationService.getAll();
    assertThat(locations.size(), is(2));
    assertThat(locations.get(0), is(l1));
    assertThat(locations.get(1), is(l2));
  }

  @Test
  void getOne() {
    locationRepository.save(l1);
    LocationEntity location = locationService.getOne(NAME_1);
    assertThat(location, is(l1));
  }

  @Test
  void getOneFails() {
    locationRepository.save(l1);
    assertThrows(NoSuchElementException.class, () -> locationService.getOne(NAME_2));
  }

  @Test
  void search() {
    locationRepository.save(l1);
    locationRepository.save(l2);
    List<LocationEntity> locations = locationService.search("", "", "restaurant");
    assertThat(locations.size(), is(2));
    assertThat(locations.get(0), is(l1));
    assertThat(locations.get(1), is(l2));
  }

  @Test
  void update() {
    locationRepository.save(l1);
    LocationEntity newLocation =
        locationService.update(NAME_1, "Alex's", "Mediterranean", "5678 Broadway", "museum");
    assertThat(locationService.getAll().size(), is(1));
    assertThat(newLocation.getName(), is("Alex's"));
    assertThat(newLocation.getDescription(), is("Mediterranean"));
    assertThat(newLocation.getAddress(), is("5678 Broadway"));
    assertThat(newLocation.getType(), is(Type.fromString("museum")));
  }

  @Test
  void updateFails() {
    assertThrows(
        NoSuchElementException.class,
        () -> locationService.update(NAME_1, "Alex's", "Mediterranean", "5678 Broadway", "museum"));
  }

  @Test
  void assignOwnerFails() {
    assertThrows(NoSuchElementException.class, () -> locationService.assignOwner(NAME_1, "cat"));
  }

  @Test
  void getRatingHistogram() {
    locationRepository.save(l1);
    userRepository.save(u1);
    reviewRepository.save(r1);
    Map<String, String> histogram = locationService.getRatingHistogram(NAME_1);
    assertThat(histogram.get("1"), is("0"));
    assertThat(histogram.get("2"), is("0"));
    assertThat(histogram.get("3"), is("1"));
    assertThat(histogram.get("4"), is("0"));
    assertThat(histogram.get("5"), is("0"));
  }

  @Test
  void getRatingHistogramFails() {
    assertThrows(NoSuchElementException.class, () -> locationService.getRatingHistogram(NAME_1));
  }

  @Test
  void createNew() {
    LocationEntity newLocation =
        locationService.createNew("Alex's", "Mediterranean", "5678 Broadway", "museum");
    assertThat(locationService.getAll().size(), is(1));
    assertThat(newLocation.getName(), is("Alex's"));
    assertThat(newLocation.getDescription(), is("Mediterranean"));
    assertThat(newLocation.getAddress(), is("5678 Broadway"));
    assertThat(newLocation.getType(), is(Type.fromString("museum")));
  }

  @Test
  void createNewFails() {
    locationRepository.save(l1);
    assertThrows(
        IllegalArgumentException.class,
        () -> locationService.createNew(NAME_1, "Mediterranean", "5678 Broadway", "museum"));
  }

  @Test
  void delete() {
    locationRepository.save(l1);
    assertThat(locationService.delete(NAME_1), is(l1));
  }

  @Test
  void deleteFails() {
    assertThrows(NoSuchElementException.class, () -> locationService.delete(NAME_1));
  }

  @Test
  void averageRating() {
    locationRepository.save(l1);
    userRepository.save(u1);
    reviewRepository.save(r1);
    assertThat(locationService.averageRating(NAME_1), is(3.0));
  }

  @Test
  void averageRatingFails() {
    assertThrows(NoSuchElementException.class, () -> locationService.averageRating(NAME_1));
  }
}
