package io.zetch.app.domain.review;

import io.zetch.app.domain.restaurant.RestaurantDto;
import io.zetch.app.domain.user.UserDto;
import java.io.Serializable;
import lombok.*;

@Value
@Builder
@AllArgsConstructor
public class ReviewDto implements Serializable {
  @ToString.Exclude Long id;
  String comment;
  Integer rating;
  UserDto user;
  RestaurantDto restaurant;
}
