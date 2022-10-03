package io.zetch.app.domain.restaurant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

import java.io.Serializable;

/** A Data Transfer Object for the {@link RestaurantEntity} entity */
@Value //Already marks fields as private and final
@Builder
@AllArgsConstructor
public class RestaurantDto implements Serializable {
  @ToString.Exclude
  Long id;
  String name;
  String cuisine;
  String address;
}
