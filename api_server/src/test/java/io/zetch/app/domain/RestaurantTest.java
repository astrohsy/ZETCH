package io.zetch.app.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class RestaurantTest {
    @Test
    public void testConstructorAndGetters() {
        User u1 = new User("b123", "Bob", "bob@example.com");
        User u2 = new User("c123", "Cat", "cat@example.com");

        Restaurant r = new Restaurant("Bob's restaurant", "American", "NY");

        assertNull(r.getId());
        assertThat(r.getName(), is("Bob's restaurant"));
        assertThat(r.getCuisine(), is("American"));
        assertThat(r.getAddress(), is("NY"));
        assertThat(r.getOwners().isEmpty(), is(true));
    }

    @Test
    public void equalsHashcodeVerify() {
        User u1 = new User("b123", "Bob", "bob@example.com");
        User u2 = new User("c123", "Cat", "cat@example.com");
        List<User> owners = List.of(u1, u2);

        Restaurant r1 = new Restaurant("Bob's restaurant", "American", "NY");
        r1.setId(1L);
        Restaurant r2 = new Restaurant("Bob's restaurant", "American", "NY");
        r2.setId(1L);

        assertThat(r1, is(r2));
        assertThat(r1.hashCode(), is(r2.hashCode()));
    }
}
