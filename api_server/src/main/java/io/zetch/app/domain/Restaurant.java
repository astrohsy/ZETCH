/**
 * Restaurant entity definition
 */
package io.zetch.app.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToMany  // A restaurant might have multiple owners; a User might own multiple restaurants
    private List<User> owners;

    @Column(nullable = false)  // Every restaurant must have a name
    private String name;

    @Column
    private String cuisine;

    @Column
    private String address;

    public Restaurant(List<User> owner, String name, String cuisine, String address) {
        this.owners = owner;
        this.name = name;
        this.cuisine = cuisine;
        this.address = address;
    }

    protected Restaurant() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getOwners() {
        return owners;
    }

    public void setOwners(List<User> owner) {
        this.owners = owner;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCuisine() {
        return cuisine;
    }

    public void setCuisine(String cuisine) {
        this.cuisine = cuisine;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Restaurant that = (Restaurant) o;
        return id.equals(that.id) && Objects.equals(owners, that.owners) && name.equals(that.name) &&
                Objects.equals(cuisine, that.cuisine) && Objects.equals(address, that.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, owners, name, cuisine, address);
    }

    @Override
    public String toString() {
        return String.format(
                "Restaurant{id=%s, owner_usernames=%s, name=%s, cuisine=%s, address=%s}",
                id,
                owners,
                name,
                cuisine,
                address
        );
    }
}
