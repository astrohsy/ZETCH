package io.zetch.app.domain;

import org.junit.jupiter.api.Test;
import static org.hamcrest.MatcherAssert.*;
import static org.hamcrest.core.Is.*;

public class UserTest {
    @Test
    public void testConstructorAndGetters() {
        User u = new User("b123", "Bob", "bob@example.com");

        assertThat(u.getUsername(), is("b123"));
        assertThat(u.getName(), is("Bob"));
        assertThat(u.getEmail(), is("bob@example.com"));
        assertThat(u.getOwnedRestaurants().isEmpty(), is(true));
    }

    @Test
    public void equalsHashcodeVerify() {
        User u1 = new User("b123", "Bob", "bob@example.com");
        User u2 = new User("b123", "Bob", "bob@example.com");

        assertThat(u1, is(u2));
        assertThat(u1.hashCode(), is(u2.hashCode()));
    }
}
