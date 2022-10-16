package io.zetch.app.domain.location;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

/** A Data Transfer Object for the {@link LocationEntity} entity */
@Value // Already marks fields as private and final
@Builder
@AllArgsConstructor
public class LocationDto implements Serializable {
  String name;
  String cuisine;
  String address;
}
