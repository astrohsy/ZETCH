/**
 * User entity definition
 */
package io.zetch.app.domain;

import javax.persistence.*;
import java.util.List;

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
