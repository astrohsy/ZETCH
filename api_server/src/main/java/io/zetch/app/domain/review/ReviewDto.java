package io.zetch.app.domain.review;

import java.io.Serializable;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ReviewDto implements Serializable {
  @ToString.Exclude Long id;
  String comment;
  Integer rating;
}
