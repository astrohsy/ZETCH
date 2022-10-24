package io.zetch.app.domain.review;

import io.zetch.app.domain.location.LocationGetDto;
import io.zetch.app.domain.user.UserGetDto;
import java.io.Serializable;

/** DTO for getting Reviews. */
public record ReviewGetDto(
    Long id, String comment, Integer rating, UserGetDto user, LocationGetDto location)
    implements Serializable {}
