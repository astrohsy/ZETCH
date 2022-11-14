package io.zetch.app.seed;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import java.util.List;

/** Sample locations. */
public class SeedLocations {

  private SeedLocations() {
    throw new IllegalStateException("Utility class");
  }

  public static final LocationEntity LOCATION1 =
      LocationEntity.builder()
          .name("Maggiano's Pizza")
          .description("Italian")
          .address("1100 Riverside Drive")
          .type(Type.RESTAURANT)
          .owners(List.of(SeedUsers.USER2))
          .build();
  public static final LocationEntity LOCATION2 =
      LocationEntity.builder()
          .name("Fireball Restaurant")
          .description("Caribbean")
          .address("3920 Broadway")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER3))
          .build();
  public static final LocationEntity LOCATION3 =
      LocationEntity.builder()
          .name("Havana Heights")
          .description("Cuban")
          .address("4029 Morningside Drive")
          .type(Type.BUILDING)
          .owners(List.of(SeedUsers.USER4))
          .build();
  public static final LocationEntity LOCATION4 =
      LocationEntity.builder()
          .name("Wimpy's")
          .description("American")
          .address("1323 St. Nicholas Ave")
          .owners(List.of(SeedUsers.USER2))
          .type(Type.RESTAURANT)
          .build();
  public static final LocationEntity LOCATION5 =
      LocationEntity.builder()
          .name("Eugene Museum of Music")
          .description(
              "A museum dedicated to preservation and celebration of "
                  + "the history of all good music, this idea for the "
                  + "museum was conceived in 1995 by generous philanthropist Eugene Kim."
          )
          .address("263th E 103 St")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER2))
          .build();

  public static final LocationEntity LOCATION6 =
      LocationEntity.builder()
          .name("Columbia Museum")
          .description(
              "Located on the campus of Columbia University, this museum plays "
                  + "a major role in developing and collecting modern art, and is "
                  + "often identified as one of the largest and most influential "
                  + "museums of modern art in the world. "
          )
          .address("569th W 120 St")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER2))
          .build();

  public static final List<LocationEntity> LOCATIONS =
      List.of(LOCATION1, LOCATION2, LOCATION3, LOCATION4, LOCATION5, LOCATION6);
}
