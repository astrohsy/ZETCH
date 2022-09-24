package io.zetch.app.domain;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class RestaurantTest {
  @Test
  public void testConstructorAndGetters() {
    Restaurant r = new Restaurant("Bob's restaurant", "American", "NY");

    assertNull(r.getId());
    assertThat(r.getName(), is("Bob's restaurant"));
    assertThat(r.getCuisine(), is("American"));
    assertThat(r.getAddress(), is("NY"));
    assertThat(r.getOwners().isEmpty(), is(true));
  }

  @Test
  public void equalsHashcodeVerify() {
    Restaurant r1 = new Restaurant("Bob's restaurant", "American", "NY");
    r1.setId(1L);
    Restaurant r2 = new Restaurant("Bob's restaurant", "American", "NY");
    r2.setId(1L);

    assertThat(r1, is(r2));
    assertThat(r1.hashCode(), is(r2.hashCode()));
  }
}
