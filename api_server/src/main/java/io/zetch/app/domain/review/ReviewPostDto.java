package io.zetch.app.domain.review;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

public record ReviewPostDto(
    String comment,
    Integer rating,
    @JsonProperty("user_id") Long userId,
    @JsonProperty("location_id") Long locationId)
    implements Serializable {}
