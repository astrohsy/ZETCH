package io.zetch.app.domain.location;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.zetch.app.domain.log.LogEntity;
import java.io.Serializable;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/** A DTO for the {@link LogEntity} entity's rating histogram. */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LocationRatingHistogramDto implements Serializable {
  @JsonProperty("rating_histogram")
  private Map<String, String> ratingHistogram;
}
