package io.zetch.app.domain.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.text.DecimalFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A DTO for the {@link LocationEntity} entity's average rating. */
@Getter
@Setter
public class LocationAvgRatingDto implements Serializable {
  @JsonProperty("average_rating")
  String averageRating;

  public LocationAvgRatingDto(double averageRating) {
    DecimalFormat f = new DecimalFormat("#.0");
    this.averageRating = f.format(averageRating);
  }
}
