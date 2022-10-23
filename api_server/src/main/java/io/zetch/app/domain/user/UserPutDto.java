package io.zetch.app.domain.user;

import java.io.Serializable;

/** A DTO for the {@link UserEntity} entity. */
public record UserPutDto(String name, String email, String affiliation) implements Serializable {}
