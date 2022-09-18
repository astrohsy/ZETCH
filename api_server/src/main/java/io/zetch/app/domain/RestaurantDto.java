package io.zetch.app.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Data Transfer Object for the {@link Restaurant} entity
 */
public class RestaurantDto implements Serializable {
    private final String ownerUsername;
    @NotNull  // Every restaurant must have a name
    private final String name;
    private final String cuisine;
    private final String address;

    /**
     * Constructor to fully initialize Restaurant Dto
     *
     * @param ownerUsername Username of the restaurant's owner
     * @param name Restaurant's name
     * @param cuisine Restaurant's cuisine
     * @param address Restaurant's address
     */
    public RestaurantDto(String ownerUsername, String name, String cuisine, String address) {
        this.ownerUsername = ownerUsername;
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
    }

    /**
     * Construct RestaurantDto from a fully instantiated {@link Restaurant}
     *
     * @param restaurant Restaurant object
     */
    public RestaurantDto(Restaurant restaurant) {
        this(restaurant.getOwner().getUsername(), restaurant.getName(),
                restaurant.getCuisine(), restaurant.getAddress());
    }

    public String getOwnerUsername() {
        return ownerUsername;
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
        RestaurantDto entity = (RestaurantDto) o;
        return Objects.equals(this.ownerUsername, entity.ownerUsername) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.cuisine, entity.cuisine) &&
                Objects.equals(this.address, entity.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerUsername, name, cuisine, address);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "ownerUsername = " + ownerUsername + ", " +
                "name = " + name + ", " +
                "cuisine = " + cuisine + ", " +
                "address = " + address + ")";
    }
}
