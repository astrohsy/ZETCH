package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import io.zetch.app.security.CognitoService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  private static final String USERNAME = "bob";
  private static final String NAME = "Bob";
  private static final String EMAIL = "bob@example.com";
  private static final String AFFILIATION_STUDENT = "student";

  @Mock private UserRepository userRepositoryMock;
  @Mock private UserEntity userMock;
  @Mock private CognitoService cognitoServiceMock;
  @InjectMocks private UserService service;

  // VERIFY SERVICE RETURN VALUE

  @Test
  public void getOne() {
    when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(Optional.of(userMock));
    assertThat(service.getOne(USERNAME), is(userMock));
  }

  @Test
  public void getAll() {
    when(userRepositoryMock.findAll()).thenReturn(List.of(userMock, userMock, userMock));
    assertThat(service.getAll().size(), is(3));
    assertThat(service.getAll().get(0), is(userMock));
  }

  // VERIFY INVOCATION OF DEPENDENCIES

  @Test
  public void update() {
    when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(Optional.of(userMock));
    service.update(USERNAME, NAME, EMAIL, AFFILIATION_STUDENT);

    // Verify save() invoked
    verify(userRepositoryMock).save(any(UserEntity.class));

    // Verify setter methods invoked
    verify(userMock).setDisplayName(NAME);
    verify(userMock).setEmail(EMAIL);
    verify(userMock).setAffiliation(Affiliation.STUDENT);
  }

  @Test
  public void delete() {
    when(userRepositoryMock.findByUsername(USERNAME)).thenReturn(Optional.of(userMock));
    service.delete(USERNAME);

    // Verify delete() invoked
    verify(userRepositoryMock).delete(any(UserEntity.class));
  }

  // VERIFY INVOCATION OF DEPS + CAPTURE PARAMETER VALUES + VERIFY PARAMETERS

  @Test
  public void createNew() {
    // Prepare to capture a User object
    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);

    service.createNew(USERNAME, NAME, EMAIL, AFFILIATION_STUDENT);

    // Verify save() invoked
    verify(userRepositoryMock).save(userCaptor.capture());
    verify(cognitoServiceMock).signUp(USERNAME);

    // Verify the attributes of the User object
    UserEntity value = userCaptor.getValue();
    assertThat(value.getUsername(), is(USERNAME));
    assertThat(value.getDisplayName(), is(NAME));
    assertThat(value.getEmail(), is(EMAIL));
    assertThat(value.getAffiliation(), is(Affiliation.STUDENT));
    assertThat(value.getOwnedRestaurants().isEmpty(), is(true));
  }
}
