package io.zetch.app.domain.review;

import io.zetch.app.domain.restaurant.RestaurantDto;
import io.zetch.app.domain.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
@AllArgsConstructor
public class ReviewPostDto implements Serializable {
  @ToString.Exclude Long id;
  String comment;
  Integer rating;
  String user_id;
  Long restaurant_id;
}
