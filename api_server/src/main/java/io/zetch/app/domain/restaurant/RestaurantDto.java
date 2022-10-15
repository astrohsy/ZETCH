package io.zetch.app.domain.restaurant;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

/** A Data Transfer Object for the {@link RestaurantEntity} entity */
@Value // Already marks fields as private and final
@Builder
@AllArgsConstructor
public class RestaurantDto implements Serializable {
  String name;
  String cuisine;
  String address;
}
