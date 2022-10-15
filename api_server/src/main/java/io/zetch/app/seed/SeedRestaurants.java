package io.zetch.app.seed;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import java.util.List;

public class SeedRestaurants {

  private static final RestaurantEntity RESTAURANT_1 =
      RestaurantEntity.builder()
          .name("Maggiano's Pizza")
          .cuisine("Italian")
          .address("1100 Riverside Drive")
          .build();
  private static final RestaurantEntity RESTAURANT_2 =
      RestaurantEntity.builder()
          .name("Fireball Restaurant")
          .cuisine("Caribbean")
          .address("3920 Broadway")
          .build();
  private static final RestaurantEntity RESTAURANT_3 =
      RestaurantEntity.builder()
          .name("Havana Heights")
          .cuisine("Cuban")
          .address("4029 Morningside Drive")
          .build();
  private static final RestaurantEntity RESTAURANT_4 =
      RestaurantEntity.builder()
          .name("Wimpy's")
          .cuisine("American")
          .address("1323 St. Nicholas Ave")
          .build();
  private static final RestaurantEntity RESTAURANT_5 =
      RestaurantEntity.builder()
          .name("Wahi's Diner")
          .cuisine("Diner")
          .address("569th W 120 St")
          .build();

  public static final List<RestaurantEntity> RESTAURANTS =
      List.of(RESTAURANT_1, RESTAURANT_2, RESTAURANT_3, RESTAURANT_4, RESTAURANT_5);
}
