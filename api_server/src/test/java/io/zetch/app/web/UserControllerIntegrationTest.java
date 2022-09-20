package io.zetch.app.web;

import io.zetch.app.domain.UserDto;
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

/**
 * DirtiesContext is used to create a brand new application context for each test.
 * This ensures that the in-memory database is dropped between each test (at the cost of performance).
 */
@SpringBootTest(webEnvironment = RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class UserControllerIntegrationTest {

    private static final String USERS_ENDPOINT = "/users/";

    private static final String USERNAME_1 = "bob";
    private static final String NAME_1 = "Bob";
    private static final String EMAIL_1 = "bob@example.com";
    private static final String USERNAME_2 = "cat";
    private static final String NAME_2 = "Cat";
    private static final String EMAIL_2 = "cat@example.com";

    @Autowired
    private TestRestTemplate restTemplate;

    /**
     * Test adding and retrieving two users
     */
    @Test
    public void postAndGetTwoUsers() {
        UserDto request1 = new UserDto(USERNAME_1, NAME_1, EMAIL_1);
        UserDto request2 = new UserDto(USERNAME_2, NAME_2, EMAIL_2);

        ResponseEntity<String> response = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("User saved"));

        ResponseEntity<String> response2 = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request2), new ParameterizedTypeReference<>() {});

        assertThat(response2.getStatusCode(), is(HttpStatus.OK));
        assertThat(response2.getBody(), is("User saved"));

        ResponseEntity<List<UserDto>> response3 = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});

        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
        assertThat(Objects.requireNonNull(response3.getBody()).size(), is(2));
        assertThat(response3.getBody().get(0), is(request1));
        assertThat(response3.getBody().get(1), is(request2));
    }

    /**
     * Test adding and retrieving a user by username
     */
    @Test
    public void postAndGetById() {
        UserDto request1 = new UserDto(USERNAME_1, NAME_1, EMAIL_1);
        UserDto request2 = new UserDto(USERNAME_2, NAME_2, EMAIL_2);

        ResponseEntity<String> response = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        ResponseEntity<String> response2 = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request2), new ParameterizedTypeReference<>() {});

        ResponseEntity<UserDto> response3 = restTemplate.exchange(USERS_ENDPOINT + USERNAME_1,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(response3.getStatusCode(), is(HttpStatus.OK));
        assertThat(response3.getBody(), is(request1));
    }

    /**
     * Test adding and retrieving non-existing username
     */
    @Test
    public void postAndGetByIdNotFound() {
        UserDto request1 = new UserDto(USERNAME_1, NAME_1, EMAIL_1);

        ResponseEntity<String> response = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        ResponseEntity<String> response2 = restTemplate.exchange(USERS_ENDPOINT + USERNAME_2,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {});

        assertThat(response2.getStatusCode(), is(HttpStatus.NOT_FOUND));
        assertThat(response2.getBody(), is("User does not exist: " + USERNAME_2));
    }

    /**
     * Test adding duplicate user
     */
    @Test
    public void postDuplicateUser() {
        UserDto request1 = new UserDto(USERNAME_1, NAME_1, EMAIL_1);

        ResponseEntity<String> response = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        assertThat(response.getStatusCode(), is(HttpStatus.OK));
        assertThat(response.getBody(), is("User saved"));

        ResponseEntity<String> response2 = restTemplate.exchange(USERS_ENDPOINT, HttpMethod.POST,
                new HttpEntity<>(request1), new ParameterizedTypeReference<>() {});

        assertThat(response2.getStatusCode(), is(HttpStatus.BAD_REQUEST));
        assertThat(response2.getBody(), is("Username unavailable: " + USERNAME_1));
    }
}
