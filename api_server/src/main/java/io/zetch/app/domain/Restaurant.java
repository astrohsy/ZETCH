/**
 * Restaurant entity definition
 */
package io.zetch.app.domain;

import javax.persistence.*;

@Entity
public class Restaurant {

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne  // A restaurant can only have one owner
    private User owner;

    @Column(nullable = false)  // Every restaurant must have a name
    private String name;

    @Column
    private String cuisine;

    @Column
    private String address;

    public Restaurant(User owner, String name, String cuisine, String address) {
        this.owner = owner;
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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
    public String toString() {
        return String.format(
                "Restaurant{id=%s, owner_username=%s, name=%s, cuisine=%s, address=%s}",
                id,
                owner.getUsername(),
                name,
                cuisine,
                address
        );
    }
}
