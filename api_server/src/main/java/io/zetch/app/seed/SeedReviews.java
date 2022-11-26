package io.zetch.app.seed;

import io.zetch.app.domain.review.ReviewEntity;
import java.util.List;

/** Helper class for adding sample reviews. */
public class SeedReviews {

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
          .location(SeedLocations.LOCATION5)
          .build();

  //  5-Stars
  public static final ReviewEntity REVIEW6 =
      ReviewEntity.builder()
          .comment(
              "I love the activities and interaction that they offered. "
                  + "I like this museum better than Guggenheim.")
          .rating(5)
          .user(SeedUsers.USER1)
          .location(SeedLocations.LOCATION5)
          .build();

  public static final ReviewEntity REVIEW7 =
      ReviewEntity.builder()
          .comment(
              "My new favorite art museum. There’s about 6 levels and over "
                  + "400+ rooms of art here from sculptures, paintings, to multi-media")
          .rating(5)
          .user(SeedUsers.USER3)
          .location(SeedLocations.LOCATION6)
          .build();

  public static final ReviewEntity REVIEW8 =
      ReviewEntity.builder()
          .comment(
              "Came here on a Saturday for my birthday weekend and had a great time! "
                  + "The museum is so spacious that even on a busy day, "
                  + "you have your own space to move around and admire the artwork.")
          .rating(5)
          .user(SeedUsers.USER4)
          .location(SeedLocations.LOCATION7)
          .build();

  public static final ReviewEntity REVIEW9 =
      ReviewEntity.builder()
          .comment(
              "Overall it's a good museum but there are a few "
                  + "things that can be improved for a better experience of visitors.")
          .rating(5)
          .user(SeedUsers.USER5)
          .location(SeedLocations.LOCATION8)
          .build();

  public static final ReviewEntity REVIEW10 =
      ReviewEntity.builder()
          .comment(
              "Fantastic collection, audio guides of most major pieces "
                  + "of art available on your phone, and free WiFi to "
                  + "access it without any restrictions.")
          .rating(5)
          .user(SeedUsers.USER6)
          .location(SeedLocations.LOCATION5)
          .build();

  public static final ReviewEntity REVIEW11 =
      ReviewEntity.builder()
          .comment(
              "Excellent exhibits, can spend all day covering wide range!"
                  + " Go back again and again to enjoy new exhibits periodically. ")
          .rating(5)
          .user(SeedUsers.USER7)
          .location(SeedLocations.LOCATION6)
          .build();

  public static final ReviewEntity REVIEW12 =
      ReviewEntity.builder()
          .comment(
              "Wonderful exhibition of art in its different times, "
                  + "you have 5 floors of a wide variety of works by great artists."
                  + " It is easy to walk between the different rooms"
                  + " as they are all connected to each other.")
          .rating(5)
          .user(SeedUsers.USER8)
          .location(SeedLocations.LOCATION7)
          .build();

  public static final ReviewEntity REVIEW13 =
      ReviewEntity.builder()
          .comment(
              "An essential Manhattan destination. "
                  + "The recent renovations made it brighter."
                  + " Supreme people watching. "
                  + "Temporary exhibitions tend to tell good stories")
          .rating(4)
          .user(SeedUsers.USER9)
          .location(SeedLocations.LOCATION8)
          .build();

  // 4 Stars
  public static final ReviewEntity REVIEW14 =
      ReviewEntity.builder()
          .comment(
              "Some of the pieces were very outlandish and thought provoking,"
                  + " while others were mundane yet done in such a way that"
                  + " makes you rethink your own realities.")
          .rating(4)
          .user(SeedUsers.USER10)
          .location(SeedLocations.LOCATION5)
          .build();

  public static final ReviewEntity REVIEW15 =
      ReviewEntity.builder()
          .comment(
              "It was worth it. Art works from amazing and highly recognized artists,"
                  + " hundreds of different paintings to see… I really enjoyed it,"
                  + " I can even say one of the best art museums in new York. ")
          .rating(4)
          .user(SeedUsers.USER11)
          .location(SeedLocations.LOCATION6)
          .build();

  public static final ReviewEntity REVIEW16 =
      ReviewEntity.builder()
          .comment(
              "Head straight to the top floor! Famous stuff is here,"
                  + " and it’s get more modern as you descend floors. "
                  + "Beautiful building, nice collections,"
                  + " but almost all the best one and "
                  + "so the crowds are concentrated at the top. ")
          .rating(4)
          .user(SeedUsers.USER12)
          .location(SeedLocations.LOCATION7)
          .build();

  // 3 Star
  public static final ReviewEntity REVIEW17 =
      ReviewEntity.builder()
          .comment(
              "The first Friday of the month is free for local "
                  + "New Yorkers to visit but you need to book the"
                  + " tickets in advance. Also they have free pen and "
                  + "paper and even chairs for visitors to participate drawing. ")
          .rating(3)
          .user(SeedUsers.USER13)
          .location(SeedLocations.LOCATION8)
          .build();

  public static final ReviewEntity REVIEW18 =
      ReviewEntity.builder()
          .comment(
              "The first Friday of the month is free for local "
                  + "New Yorkers to visit but you need to book the"
                  + " tickets in advance. Also they have free pen and "
                  + "paper and even chairs for visitors to participate drawing. ")
          .rating(3)
          .user(SeedUsers.USER14)
          .location(SeedLocations.LOCATION5)
          .build();

  public static final ReviewEntity REVIEW19 =
      ReviewEntity.builder()
          .comment(
              "Other the seeing Starry Night up close, the monet painting, "
                  + "and a painting by Edward Hopper there is "
                  + "not much else interesting to look at here. "
                  + "Everything else in the museum is tasteless modern art. ")
          .rating(2)
          .user(SeedUsers.USER15)
          .location(SeedLocations.LOCATION6)
          .build();

  public static final ReviewEntity REVIEW20 =
      ReviewEntity.builder()
          .comment(
              "It was very disappointing that they old us tickets "
                  + "for the same evening (first Friday of the month) "
                  + "that it was a pay what you wish hour. The museum was crazy busy. ")
          .rating(2)
          .user(SeedUsers.USER16)
          .location(SeedLocations.LOCATION7)
          .build();
  public static final ReviewEntity REVIEW21 =
      ReviewEntity.builder()
          .comment(
              "This was an amazing, if not slightly bizarre experience. "
                  + "We came here after seeing it was included on the New York pass. ")
          .rating(2)
          .user(SeedUsers.USER15)
          .location(SeedLocations.LOCATION8)
          .build();

  public static final ReviewEntity REVIEW22 =
      ReviewEntity.builder()
          .comment(
              "Awesome place to visit. It started off slow but by the end"
                  + " it was well worth the price of admission. "
                  + "You're met with a couple galleries to start with "
                  + "but it really turns up with the games. ")
          .rating(2)
          .user(SeedUsers.USER16)
          .location(SeedLocations.LOCATION5)
          .build();

  // 1 Star
  public static final List<ReviewEntity> REVIEWS =
      List.of(
          REVIEW1, REVIEW2, REVIEW3, REVIEW4, REVIEW5, REVIEW6, REVIEW7, REVIEW8, REVIEW9, REVIEW10,
          REVIEW11, REVIEW12, REVIEW13, REVIEW14, REVIEW15, REVIEW16, REVIEW17, REVIEW18, REVIEW19,
          REVIEW20, REVIEW21, REVIEW22);

  private SeedReviews() {
    throw new IllegalStateException("Utility class");
  }
}
