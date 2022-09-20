package io.zetch.app.service;

import io.zetch.app.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.transaction.Transactional;

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.StringContains.containsString;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
@Transactional
public class UserServiceIntegrationTest {

    private static final String USERNAME = "bob";
    private static final String INVALID_USERNAME = "cat";
    private static final String NAME = "Bob";
    private static final String EMAIL = "bob@example.com";

    @Autowired
    private UserService userService;

    @Test
    public void createNew() {
        userService.createNew(USERNAME, NAME, EMAIL);

        // Verify User created
        User newUser = userService.verifyUser(USERNAME);
        assertThat(newUser.getUsername(), is(USERNAME));
        assertThat(newUser.getName(), is(NAME));
        assertThat(newUser.getEmail(), is(EMAIL));
        assertThat(newUser.getOwnedRestaurants().isEmpty(), is(true));
    }

    @Test
    public void createNewFailsWhenUsernameExists() {
        userService.createNew(USERNAME, NAME, EMAIL);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.createNew(USERNAME, NAME, EMAIL);
        });

        String expectedMsg = "Username unavailable";
        String actualMsg = exception.getMessage();

        assertThat(actualMsg, containsString(expectedMsg));
    }

    @Test
    public void update() {
        createNew();
        User user = userService.update(USERNAME, "newName", "newEmail");
        assertThat(user.getUsername(), is(USERNAME));
        assertThat(user.getName(), is("newName"));
        assertThat(user.getEmail(), is("newEmail"));
    }

    @Test
    public void updateFailsWhenUserNotFound() {
        Exception exception = assertThrows(NoSuchElementException.class, () -> {
            userService.update(INVALID_USERNAME, "newName", "newEmail");
        });

        String expectedMsg = "User does not exist";
        String actualMsg = exception.getMessage();

        assertThat(actualMsg, containsString(expectedMsg));
    }
}
