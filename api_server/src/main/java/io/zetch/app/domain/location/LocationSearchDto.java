package io.zetch.app.domain.location;

import java.io.Serializable;

/** A Data Transfer Object for the {@link LocationEntity} entity. */
public record LocationSearchDto(String name, String description, String type)
    implements Serializable {}
