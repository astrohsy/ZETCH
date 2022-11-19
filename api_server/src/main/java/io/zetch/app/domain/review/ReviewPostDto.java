package io.zetch.app.domain.review;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/** DTO for posting reviews. */
public record ReviewPostDto(
    String comment,
    @Min(1) @Max(5) Integer rating,
    @JsonProperty("user_id") Long userId,
    @JsonProperty("location_id") Long locationId)
    implements Serializable {}
