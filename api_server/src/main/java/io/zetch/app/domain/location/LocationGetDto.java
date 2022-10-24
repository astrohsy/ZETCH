package io.zetch.app.domain.location;

import java.io.Serializable;

/** A Data Transfer Object for the {@link LocationEntity} entity */
public record LocationGetDto(Long id, String name, String description, String address, String type)
    implements Serializable {}
