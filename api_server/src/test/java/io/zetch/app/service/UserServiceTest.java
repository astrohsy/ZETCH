package io.zetch.app.service;

import io.zetch.app.domain.Restaurant;
import io.zetch.app.domain.User;
import io.zetch.app.repo.RestaurantRepository;
import io.zetch.app.repo.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    private static final String USERNAME = "bob";
    private static final String NAME = "Bob";
    private static final String EMAIL = "bob@example.com";

    @Mock
    private UserRepository userRepositoryMock;
    @InjectMocks
    private UserService service;
    @Mock
    private User userMock;

    // VERIFY SERVICE RETURN VALUE

    @Test
    public void getOne() {
        when(userRepositoryMock.findById(USERNAME)).thenReturn(Optional.of(userMock));
        assertThat(service.getOne(USERNAME), is(userMock));
    }

    @Test
    public void getAll() {
        when(userRepositoryMock.findAll()).thenReturn(List.of(userMock));
        assertThat(service.getAll().get(0), is(userMock));
    }

    // VERIFY INVOCATION OF DEPENDENCIES

    @Test
    public void update() {
        when(userRepositoryMock.findById(USERNAME)).thenReturn(Optional.of(userMock));
        service.update(USERNAME, NAME, EMAIL);

        // Verify save() invoked
        verify(userRepositoryMock).save(any(User.class));

        // Verify setter methods invoked
        verify(userMock).setName(NAME);
        verify(userMock).setEmail(EMAIL);
    }

    // VERIFY INVOCATION OF DEPS + CAPTURE PARAMETER VALUES + VERIFY PARAMETERS

    @Test
    public void createNew() {
        // Prepare to capture a User object
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        service.createNew(USERNAME, NAME, EMAIL);

        // Verify save() invoked
        verify(userRepositoryMock).save(userCaptor.capture());

        // Verify the attributes of the User object
        User value = userCaptor.getValue();
        assertThat(value.getUsername(), is(USERNAME));
        assertThat(value.getName(), is(NAME));
        assertThat(value.getEmail(), is(EMAIL));
    }
}