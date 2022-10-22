package io.zetch.app.domain.review;

import io.zetch.app.domain.location.LocationDto;
import io.zetch.app.domain.user.UserDto;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
@NoArgsConstructor
public class ReviewGetDto extends ReviewDto {
  UserDto user;
  LocationDto location;
}
