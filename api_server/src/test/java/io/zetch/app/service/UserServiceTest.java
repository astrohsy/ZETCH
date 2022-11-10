package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  private static final String USERNAME = "bob";
  private static final String NAME = "Bob";
  private static final String EMAIL = "bob@example.com";
  private static final String AFFILIATION = "other";

  @Mock private UserRepository userRepositoryMock;
  @Mock private UserEntity userMock;
  @Mock private CognitoService cognitoServiceMock;
  @InjectMocks private UserService service;

  @Test
  void getOne() {
    when(userRepositoryMock.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(userMock));
    assertThat(service.getOne(USERNAME), is(userMock));
  }

  @Test
  void getAll() {
    when(userRepositoryMock.findAll()).thenReturn(List.of(userMock, userMock, userMock));
    assertThat(service.getAll().size(), is(3));
    assertThat(service.getAll().get(0), is(userMock));
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "New Name, null, null",
        "null, New Email, null",
        "null, null, admin",
        "New Name, New Email, admin",
      },
      nullValues = {"null"})
  void update(String newName, String newEmail, String newAffiliation) {
    UserEntity old =
        UserEntity.builder()
            .username(USERNAME)
            .displayName(NAME)
            .email(EMAIL)
            .affiliation(Affiliation.fromString(AFFILIATION))
            .build();

    UserEntity updated =
        UserEntity.builder()
            .username(USERNAME)
            .displayName(newName != null ? newName : NAME)
            .email(newEmail != null ? newEmail : EMAIL)
            .affiliation(
                newAffiliation != null
                    ? Affiliation.fromString(newAffiliation)
                    : Affiliation.fromString(AFFILIATION))
            .build();

    when(userRepositoryMock.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(old));
    service.update(USERNAME, newName, newEmail, newAffiliation);

    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);
    verify(userRepositoryMock).save(userCaptor.capture());

    UserEntity value = userCaptor.getValue();
    assertThat(value.getUsername(), is(updated.getUsername()));
    assertThat(value.getDisplayName(), is(updated.getDisplayName()));
    assertThat(value.getEmail(), is(updated.getEmail()));
    assertThat(value.getAffiliation(), is(updated.getAffiliation()));
  }

  @Test
  void delete() {
    when(userRepositoryMock.findByUsernameIgnoreCase(USERNAME)).thenReturn(Optional.of(userMock));
    service.delete(USERNAME);

    // Verify delete() invoked
    verify(userRepositoryMock).delete(any(UserEntity.class));
  }

  @Test
  void createNew() {
    // Prepare to capture a User object
    ArgumentCaptor<UserEntity> userCaptor = ArgumentCaptor.forClass(UserEntity.class);

    service.createNew(USERNAME, NAME, EMAIL, AFFILIATION);

    // Verify save() invoked
    verify(userRepositoryMock).save(userCaptor.capture());
    verify(cognitoServiceMock).signUp(USERNAME);

    // Verify the attributes of the User object
    UserEntity value = userCaptor.getValue();
    assertThat(value.getUsername(), is(USERNAME));
    assertThat(value.getDisplayName(), is(NAME));
    assertThat(value.getEmail(), is(EMAIL));
    assertThat(value.getAffiliation(), is(Affiliation.OTHER));
    assertThat(value.getOwnedLocations().isEmpty(), is(true));
  }

  @Test
  void createNew_AlreadyExists() {
    when(userRepositoryMock.existsByUsername(USERNAME)).thenReturn(true);
    assertThrows(
        IllegalArgumentException.class,
        () -> service.createNew(USERNAME, NAME, EMAIL, AFFILIATION));
  }
}
