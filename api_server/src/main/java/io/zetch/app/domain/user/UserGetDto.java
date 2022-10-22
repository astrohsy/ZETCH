package io.zetch.app.domain.user;

import java.io.Serializable;

/** A DTO for the {@link UserEntity} entity. */
public record UserGetDto(
    Long id, String username, String displayName, String email, String affiliation)
    implements Serializable {}
