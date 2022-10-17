package io.zetch.app.domain.review;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.ToString;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ReviewPostDto implements Serializable {
  @ToString.Exclude Long id;
  String comment;
  Integer rating;
  Long user_id;
  Long location_id;
}
