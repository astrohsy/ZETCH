package io.zetch.app.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/** A DTO for the {@link UserEntity} entity. */
public record UserGetDto(
    Long id,
    String username,
    @JsonProperty("display_name") String displayName,
    String email,
    String affiliation)
    implements Serializable {}
