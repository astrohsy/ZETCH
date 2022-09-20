package io.zetch.app.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Data Transfer Object for the {@link Restaurant} entity
 */
public class RestaurantDto implements Serializable {

    private final String name;
    private final String cuisine;
    private final String address;

    /**
     * Constructor to fully initialize Restaurant Dto
     *
     * @param name Restaurant's name
     * @param cuisine Restaurant's cuisine
     * @param address Restaurant's address
     */
    public RestaurantDto(String name, String cuisine, String address) {
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
