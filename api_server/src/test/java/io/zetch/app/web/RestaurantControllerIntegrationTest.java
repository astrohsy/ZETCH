package io.zetch.app.web;

import io.zetch.app.domain.RestaurantDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class RestaurantControllerIntegrationTest {
    private static final String RESTAURANT_ENDPOINT = "/restaurants/";

    private static final Long ID_1 = 1L;
    private static final String NAME_1 = "Bob's";
    private static final String CUISINE_1 = "Italian";
    private static final String ADDRESS_1 = "1234 Broadway";
    private static final Long ID_2 = 2L;
    private static final String NAME_2 = "Cat's";
    private static final String CUISINE_2 = "French";
    private static final String ADDRESS_2 = "15 Amsterdam";

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test adding and retrieving two restaurants
     */
    @Test
    public void postAndGetTwoRestaurants() {
        RestaurantDto request1 = new RestaurantDto(NAME_1, CUISINE_1, ADDRESS_1);
        RestaurantDto request2 = new RestaurantDto(NAME_2, CUISINE_2, ADDRESS_2);

        ResponseEntity<String> response1 = restTemplate.exchange(RESTAURANT_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        assertThat(response1.getBody(), is("Restaurant saved"));

        ResponseEntity<String> response2 = restTemplate.exchange(RESTAURANT_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request2), new ParameterizedTypeReference<>() {});

        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), is("Restaurant saved"));

        ResponseEntity<List<RestaurantDto>> response3 = restTemplate.exchange(RESTAURANT_ENDPOINT, HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});

        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response3.getBody()).size(), is(2));
        assertThat(response3.getBody().get(0), is(request1));
        assertThat(response3.getBody().get(1), is(request2));
    }

    /**
     * Test adding and retrieving a restaurant by ID
     */
    @Test
    public void postAndGetById() {
        RestaurantDto request1 = new RestaurantDto(NAME_1, CUISINE_1, ADDRESS_1);
        RestaurantDto request2 = new RestaurantDto(NAME_2, CUISINE_2, ADDRESS_2);

        ResponseEntity<String> response1 = restTemplate.exchange(RESTAURANT_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        assertThat(response1.getBody(), is("Restaurant saved"));

        ResponseEntity<String> response2 = restTemplate.exchange(RESTAURANT_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request2), new ParameterizedTypeReference<>() {});

        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), is("Restaurant saved"));

        ResponseEntity<RestaurantDto> response3 = restTemplate.exchange(RESTAURANT_ENDPOINT + ID_1, HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});

        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
        assertThat(response3.getBody(), is(request1));
    }

    /**
     * Test adding and retrieving non-existing restaurant
     */
    @Test
    public void postAndGetByIdNotFound() {
        RestaurantDto request1 = new RestaurantDto(NAME_1, CUISINE_1, ADDRESS_1);

        ResponseEntity<String> response1 = restTemplate.exchange(RESTAURANT_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        assertThat(response1.getStatusCode(), is(HttpStatus.OK));
        assertThat(response1.getBody(), is("Restaurant saved"));

        ResponseEntity<String> response3 = restTemplate.exchange(RESTAURANT_ENDPOINT + ID_2, HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});

        assertThat(response3.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(response3.getBody(), is("Restaurant does not exist: " + ID_2));
    }
}
