package io.zetch.app.domain.location;

import java.io.Serializable;

/** A Data Transfer Object for the {@link LocationEntity} entity */
public record LocationDto(String name, String cuisine, String address) implements Serializable {}
