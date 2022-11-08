package io.zetch.app.seed;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import java.util.List;

/** Sample locations. */
public class SeedLocations {

  private SeedLocations() {
    throw new IllegalStateException("Utility class");
  }

  public static final LocationEntity LOCATION_1 =
      LocationEntity.builder()
          .name("Maggiano's Pizza")
          .description("Italian")
          .address("1100 Riverside Drive")
          .type(Type.RESTAURANT)
          .owners(List.of(SeedUsers.USER_2))
          .build();
  public static final LocationEntity LOCATION_2 =
      LocationEntity.builder()
          .name("Fireball Restaurant")
          .description("Caribbean")
          .address("3920 Broadway")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER_3))
          .build();
  public static final LocationEntity LOCATION_3 =
      LocationEntity.builder()
          .name("Havana Heights")
          .description("Cuban")
          .address("4029 Morningside Drive")
          .type(Type.BUILDING)
          .owners(List.of(SeedUsers.USER_4))
          .build();
  public static final LocationEntity LOCATION_4 =
      LocationEntity.builder()
          .name("Wimpy's")
          .description("American")
          .address("1323 St. Nicholas Ave")
          .type(Type.RESTAURANT)
          .build();
  public static final LocationEntity LOCATION_5 =
      LocationEntity.builder()
          .name("Wahi's Diner")
          .description("Diner")
          .address("569th W 120 St")
          .type(Type.MUSEUM)
          .owners(List.of(SeedUsers.USER_5))
          .build();

  public static final List<LocationEntity> LOCATIONS =
      List.of(LOCATION_1, LOCATION_2, LOCATION_3, LOCATION_4, LOCATION_5);
}
