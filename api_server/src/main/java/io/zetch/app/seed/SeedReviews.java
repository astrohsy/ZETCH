package io.zetch.app.seed;

import io.zetch.app.domain.review.ReviewEntity;
import java.util.List;

/** Helper class for adding sample reviews. */
public class SeedReviews {

  public static ReviewEntity REVIEW_1 =
      ReviewEntity.builder()
          .comment("Great meal, I enjoyed the bacon and ranch pizza.")
          .rating(4)
          .user(SeedUsers.USER_1)
          .location(SeedLocations.LOCATION_1)
          .build();
  public static ReviewEntity REVIEW_2 =
      ReviewEntity.builder()
          .comment("Most authentic Jamaican food in Manhattan.")
          .rating(5)
          .user(SeedUsers.USER_2)
          .location(SeedLocations.LOCATION_2)
          .build();
  public static ReviewEntity REVIEW_3 =
      ReviewEntity.builder()
          .comment("The service is terrible and the food is stale.")
          .rating(1)
          .user(SeedUsers.USER_3)
          .location(SeedLocations.LOCATION_3)
          .build();
  public static ReviewEntity REVIEW_4 =
      ReviewEntity.builder()
          .comment("This museum to American dining is a gastronomical experience.")
          .rating(3)
          .user(SeedUsers.USER_4)
          .location(SeedLocations.LOCATION_4)
          .build();
  public static ReviewEntity REVIEW_5 =
      ReviewEntity.builder()
          .comment("Had a great time visiting from Michigan.")
          .rating(5)
          .user(SeedUsers.USER_5)
          .location(SeedLocations.LOCATION_4)
          .build();
  public static final List<ReviewEntity> REVIEWS =
      List.of(REVIEW_1, REVIEW_2, REVIEW_3, REVIEW_4, REVIEW_5);

  private SeedReviews() {
    throw new IllegalStateException("Utility class");
  }
}
