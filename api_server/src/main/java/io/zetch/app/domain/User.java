/**
 * User entity definition
 */
package io.zetch.app.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "zetch_user") // "User" is a reserved keyword in some DBs
public class User {
    @Id
    private String username;

    @Column
    private String name;

    @Column
    private String email;

    @ManyToMany(mappedBy = "owners")  // A restaurant might have multiple owners; a User might own multiple restaurants
    private List<Restaurant> ownedRestaurants;

    public User(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.ownedRestaurants = null;
    }

    public User(String username, String name, String email, List<Restaurant> ownedRestaurants) {
        this.username = username;
        this.name = name;
        this.email = email;
        this.ownedRestaurants = ownedRestaurants;
    }

    protected User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<Restaurant> getOwnedRestaurants() {
        return ownedRestaurants;
    }

    public void setOwnedRestaurants(List<Restaurant> ownedRestaurants) {
        this.ownedRestaurants = ownedRestaurants;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return username.equals(user.username) && Objects.equals(name, user.name) && Objects.equals(email, user.email)
                && Objects.equals(ownedRestaurants, user.ownedRestaurants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, email, ownedRestaurants);
    }

    @Override
    public String toString() {
        return String.format(
                "User{username=%s, name=%s, email=%s}",
                username,
                name,
                email
        );
    }
}
