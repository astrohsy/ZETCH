package io.zetch.app.domain;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * A Data Transfer Object for the {@link User} entity
 */
public class UserDto implements Serializable {
    private final String username;
    private final String name;
    private final String email;

    public UserDto(String username, String name, String email) {
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto entity = (UserDto) o;
        return Objects.equals(this.username, entity.username) &&
                Objects.equals(this.name, entity.name) &&
                Objects.equals(this.email, entity.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, name, email);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "(" +
                "username = " + username + ", " +
                "name = " + name + ", " +
                "email = " + email + ", ";
    }
}