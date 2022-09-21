package io.zetch.app.domain;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Data Transfer Object for the {@link Restaurant} entity
 */
public class RestaurantDto implements Serializable {

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private final Long id;
    private final String name;
    private final String cuisine;
    private final String address;

    public RestaurantDto(Long id, String name, String cuisine, String address) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public String getAddress() {
        return address;
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RestaurantDto that = (RestaurantDto) o;
        return name.equals(that.name) && Objects.equals(cuisine, that.cuisine) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, cuisine, address);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                String.format("{name=%s, cuisine=%s, address=%s}",
                        name,
                        cuisine,
                        address);
    }
}
