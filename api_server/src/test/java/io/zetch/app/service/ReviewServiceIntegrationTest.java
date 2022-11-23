package io.zetch.app.service;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ReviewServiceIntegrationTest {
  private static final String USER_NAME_1 = "cat";
  private static final String USER_NAME_2 = "dog";
  private static final String LOCATION_NAME_1 = "American Museum of Natural History";
  private static final String LOCATION_NAME_2 = "The Museum of Modern Art";
  private static final String LOCATION_DESCRIPTION_1 =
      "The American Museum of Natural History"
          + " is a natural history museum"
          + " on the Upper West Side of Manhattan in New York City.";
  private static final String LOCATION_DESCRIPTION_2 =
      "The Museum of Modern Art (MoMA) is an art museum"
          + "located in Midtown Manhattan, New York City, on 53rd Street"
          + "between Fifth and Sixth Avenues.";
  private static final String LOCATION_ADDRESS_1 = "200 Central Park West, New York, NY 10024";
  private static final String LOCATION_ADDRESS_2 = "11 W 53rd St, New York, NY 10019";
  private static final Type LOCATION_TYPE_1 = Type.fromString("museum");
  private static final Type LOCATION_TYPE_2 = Type.fromString("museum");
  UserEntity u1 =
      UserEntity.builder()
          .username(USER_NAME_1)
          .displayName("Cat")
          .email("cat@cat.cat")
          .affiliation(Affiliation.OTHER)
          .build();
  UserEntity u2 =
      UserEntity.builder()
          .username(USER_NAME_2)
          .displayName("Dog")
          .email("dog@dog.dog")
          .affiliation(Affiliation.OTHER)
          .build();

  LocationEntity l1 =
      LocationEntity.builder()
          .name(LOCATION_NAME_1)
          .description(LOCATION_DESCRIPTION_1)
          .type(LOCATION_TYPE_1)
          .address(LOCATION_ADDRESS_1)
          .build();

  LocationEntity l2 =
      LocationEntity.builder()
          .name(LOCATION_NAME_2)
          .description(LOCATION_DESCRIPTION_2)
          .type(LOCATION_TYPE_2)
          .address(LOCATION_ADDRESS_2)
          .build();

  ReviewEntity r1 =
      ReviewEntity.builder()
          .location(l1)
          .user(u1)
          .rating(3)
          .comment(
              "This museum is absolutely awesome if you are interested in history, space, animals,"
                  + " and rocks.")
          .build();

  ReviewEntity r2 =
      ReviewEntity.builder()
          .location(l1)
          .user(u2)
          .rating(4)
          .comment("It was great to see many dogs in the museum!")
          .build();
  @Autowired private ReviewService reviewService;
  @Autowired private UserRepository userRepository;

  @Autowired private LocationRepository locationRepository;

  @Autowired private ReviewRepository reviewRepository;

  @BeforeEach
  void populateDatabase() {
    userRepository.save(u1);
    userRepository.save(u2);
    locationRepository.save(l1);
    locationRepository.save(l2);
  }

  @AfterEach
  void resetDatabase() {
    reviewRepository.deleteAll();
    userRepository.deleteAll();
    locationRepository.deleteAll();
  }

  @Test
  void getAll() {
    reviewRepository.save(r1);
    reviewRepository.save(r2);
    List<ReviewEntity> reviews = reviewService.getAll();
    assertThat(reviews.size(), is(2));
    assertThat(reviews.get(0), is(r1));
    assertThat(reviews.get(1), is(r2));
  }

  @Test
  void getOne() {
    reviewRepository.save(r1);
    ReviewEntity fetchR1 = reviewService.getOne(r1.getId());
    assertThat(fetchR1, is(r1));
  }

  @Test
  void getOneFails() {
    reviewRepository.save(r1);
    Long id = r1.getId() + 1;
    assertThrows(NoSuchElementException.class, () -> reviewService.getOne(id));
  }

  @Test
  void createNew() {
    ReviewEntity newReview = reviewService.createNew("Nice museum!", 4, u1.getId(), l1.getId());
    assertThat(reviewService.getAll().size(), is(1));
    assertThat(newReview.getComment(), is("Nice museum!"));
    assertThat(newReview.getRating(), is(4));
    assertThat(newReview.getUser(), is(u1));
    assertThat(newReview.getLocation(), is(l1));
  }

  @Test
  void createNewFails() {
    Long id = u1.getId();
    Long id2 = l1.getId();
    assertThrows(
        ConstraintViolationException.class, () -> reviewService.createNew("Hello", 7, id, id2));
  }

  @Test
  void update() {
    reviewRepository.save(r1);
    ReviewEntity newReview = reviewService.update(r1.getId(), "updated", 5);
    assertThat(reviewService.getAll().size(), is(1));
    assertThat(newReview.getComment(), is("updated"));
    assertThat(newReview.getRating(), is(5));
  }

  @Test
  void updateFails() {
    assertThrows(NoSuchElementException.class, () -> reviewService.update(123L, "Bob", 4));
  }

  @Test
  void updateFails_wrongRating() {
    reviewRepository.save(r1);
    Long id = r1.getId();
    assertThrows(ConstraintViolationException.class, () -> reviewService.update(id, "Bob", 10));
  }

  @Test
  void delete() {
    reviewRepository.save(r1);
    assertThat(reviewRepository.findAll().size(), is(1));
    reviewService.deleteOne(r1.getId());
    assertThat(reviewRepository.findAll().size(), is(0));
  }

  @Test
  void deleteFails() {
    // Database is cleaned before each test
    assertThrows(NoSuchElementException.class, () -> reviewService.deleteOne(123L));
  }
}
