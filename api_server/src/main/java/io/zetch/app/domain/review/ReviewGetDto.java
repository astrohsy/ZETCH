package io.zetch.app.domain.review;

import io.zetch.app.domain.location.LocationGetDto;
import io.zetch.app.domain.user.UserGetDto;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReviewGetDto extends ReviewDto {
  UserGetDto user;
  LocationGetDto location;
}
