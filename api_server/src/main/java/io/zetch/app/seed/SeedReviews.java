package io.zetch.app.seed;

import io.zetch.app.domain.review.ReviewEntity;
import java.util.List;

/** Helper class for adding sample reviews. */
public class SeedReviews {

  private SeedReviews() {
    throw new IllegalStateException("Utility class");
  }

  public static final ReviewEntity REVIEW1 =
      ReviewEntity.builder()
          .comment("Great meal, I enjoyed the bacon and ranch pizza.")
          .rating(4)
          .user(SeedUsers.USER1)
          .location(SeedLocations.LOCATION1)
          .build();
  public static final ReviewEntity REVIEW2 =
      ReviewEntity.builder()
          .comment("Most authentic Jamaican food in Manhattan.")
          .rating(5)
          .user(SeedUsers.USER2)
          .location(SeedLocations.LOCATION2)
          .build();
  public static final ReviewEntity REVIEW3 =
      ReviewEntity.builder()
          .comment("The service is terrible and the food is stale.")
          .rating(1)
          .user(SeedUsers.USER3)
          .location(SeedLocations.LOCATION3)
          .build();
  public static final ReviewEntity REVIEW4 =
      ReviewEntity.builder()
          .comment("This museum to American dining is a gastronomical experience.")
          .rating(3)
          .user(SeedUsers.USER4)
          .location(SeedLocations.LOCATION4)
          .build();
  public static final ReviewEntity REVIEW5 =
      ReviewEntity.builder()
          .comment("Had a great time visiting from Michigan.")
          .rating(5)
          .user(SeedUsers.USER5)
          .location(SeedLocations.LOCATION4)
          .build();
  public static final List<ReviewEntity> REVIEWS =
      List.of(REVIEW1, REVIEW2, REVIEW3, REVIEW4, REVIEW5);
}
