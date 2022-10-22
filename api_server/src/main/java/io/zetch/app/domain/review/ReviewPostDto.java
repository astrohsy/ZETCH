package io.zetch.app.domain.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ReviewPostDto extends ReviewDto {
  @JsonProperty("user_id")
  Long userId;

  @JsonProperty("location_id")
  Long locationId;
}
