package io.zetch.app.domain;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * A Data Transfer Object for the {@link Restaurant} entity
 */
public class RestaurantDto implements Serializable {
    private final List<String> ownerUsernames;
    @NotNull  // Every restaurant must have a name
    private final String name;
    private final String cuisine;
    private final String address;

    /**
     * Constructor to fully initialize Restaurant Dto
     *
     * @param ownerUsernames List of usernames of the restaurant's owners
     * @param name Restaurant's name
     * @param cuisine Restaurant's cuisine
     * @param address Restaurant's address
     */
    public RestaurantDto(List<String> ownerUsernames, String name, String cuisine, String address) {
        this.ownerUsernames = ownerUsernames;
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
        this(restaurant.getOwners().stream().map(User::getUsername).collect(Collectors.toList()),
                restaurant.getName(), restaurant.getCuisine(), restaurant.getAddress());
    }

    public List<String> getOwnerUsernames() {
        return ownerUsernames;
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
        return Objects.equals(ownerUsernames, that.ownerUsernames) && name.equals(that.name)
                && Objects.equals(cuisine, that.cuisine) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ownerUsernames, name, cuisine, address);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() +
                String.format("{ownerUsernames=%s, name=%s, cuisine=%s, address=%s}",
                        ownerUsernames,
                        name,
                        cuisine,
                        address);
    }
}
