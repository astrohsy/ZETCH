package io.zetch.app.seed;

import io.zetch.app.domain.location.LocationEntity;
import java.util.List;

public class SeedLocations {

  private static final LocationEntity LOCATION_1 =
      LocationEntity.builder()
          .name("Maggiano's Pizza")
          .cuisine("Italian")
          .address("1100 Riverside Drive")
          .build();
  private static final LocationEntity LOCATION_2 =
      LocationEntity.builder()
          .name("Fireball Restaurant")
          .cuisine("Caribbean")
          .address("3920 Broadway")
          .build();
  private static final LocationEntity LOCATION_3 =
      LocationEntity.builder()
          .name("Havana Heights")
          .cuisine("Cuban")
          .address("4029 Morningside Drive")
          .build();
  private static final LocationEntity LOCATION_4 =
      LocationEntity.builder()
          .name("Wimpy's")
          .cuisine("American")
          .address("1323 St. Nicholas Ave")
          .build();
  private static final LocationEntity LOCATION_5 =
      LocationEntity.builder()
          .name("Wahi's Diner")
          .cuisine("Diner")
          .address("569th W 120 St")
          .build();

  public static final List<LocationEntity> LOCATIONS =
      List.of(LOCATION_1, LOCATION_2, LOCATION_3, LOCATION_4, LOCATION_5);
}
