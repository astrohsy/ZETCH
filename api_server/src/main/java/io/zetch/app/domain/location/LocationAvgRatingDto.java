package io.zetch.app.domain.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A DTO for the {@link LocationEntity} entity's average rating. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationAvgRatingDto implements Serializable {
  @JsonProperty("average_rating")
  long averageRating;
}
