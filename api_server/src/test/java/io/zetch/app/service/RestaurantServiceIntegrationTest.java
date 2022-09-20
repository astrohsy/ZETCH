package io.zetch.app.service;

import io.zetch.app.domain.Restaurant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
public class RestaurantServiceIntegrationTest {

    private static final Long ID = 1L;
    private static final String NAME = "Bob's";
    private static final String CUISINE = "Italian";
    private static final String ADDRESS = "1234 Broadway";

    @Autowired
    private RestaurantService restaurantService;

    @Test
    public void createNew() {
        restaurantService.createNew(NAME, CUISINE, ADDRESS);

        // Verify Restaurant created
        Restaurant newRestaurant = restaurantService.verifyRestaurant(ID);
        assertThat(newRestaurant.getName(), is(NAME));
        assertThat(newRestaurant.getCuisine(), is(CUISINE));
        assertThat(newRestaurant.getAddress(), is(ADDRESS));
        assertThat(newRestaurant.getOwners().isEmpty(), is(true));
    }
}
