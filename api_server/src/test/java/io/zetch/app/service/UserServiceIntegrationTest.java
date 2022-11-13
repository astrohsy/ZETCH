package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.hamcrest.core.Is.is;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserServiceIntegrationTest {

  private static final String NAME_1 = "cat";
  private static final String NAME_2 = "dog";
  UserEntity u1 =
      UserEntity.builder()
          .username(NAME_1)
          .displayName("Cat")
          .email("cat@cat.cat")
          .affiliation(Affiliation.OTHER)
          .build();
  UserEntity u2 =
      UserEntity.builder()
          .username(NAME_2)
          .displayName("Dog")
          .email("dog@dog.dog")
          .affiliation(Affiliation.OTHER)
          .build();
  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;

  @AfterEach
  void resetDatabase() {
    userRepository.deleteAll();
  }

  @Test
  void getAll() {
    userRepository.save(u1);
    userRepository.save(u2);
    List<UserEntity> users = userService.getAll();
    assertThat(users.size(), is(2));
    assertThat(users.get(0), is(u1));
    assertThat(users.get(1), is(u2));
  }

  @Test
  void getOne() {
    userRepository.save(u1);
    UserEntity user = userService.getOne(NAME_1);
    assertThat(user, is(u1));
  }

  @Test
  void getOneFails() {
    userRepository.save(u1);
    assertThrows(NoSuchElementException.class, () -> userService.getOne(NAME_2));
  }

  @Test
  void createNew() {
    UserEntity newUser = userService.createNew("bob", "Bob", "bob@me.com", "other");
    assertThat(userService.getAll().size(), is(1));
    assertThat(newUser.getUsername(), is("bob"));
    assertThat(newUser.getDisplayName(), is("Bob"));
    assertThat(newUser.getEmail(), is("bob@me.com"));
    assertThat(newUser.getAffiliation(), is(Affiliation.OTHER));
  }

  @Test
  void createNewFails() {
    userRepository.save(u1);
    assertThrows(
        IllegalArgumentException.class,
        () -> userService.createNew(NAME_1, "Bob", "bob@me.com", "other"));
  }

  @Test
  void update() {
    userRepository.save(u1);
    UserEntity newUser = userService.update(NAME_1, "Bob", "bob@me.com", "other");
    assertThat(userService.getAll().size(), is(1));
    assertThat(newUser.getUsername(), is(NAME_1));
    assertThat(newUser.getDisplayName(), is("Bob"));
    assertThat(newUser.getEmail(), is("bob@me.com"));
    assertThat(newUser.getAffiliation(), is(Affiliation.OTHER));
  }

  @Test
  void updateFails() {
    assertThrows(
        NoSuchElementException.class,
        () -> userService.update(NAME_1, "Bob", "bob@me.com", "other"));
  }

  @Test
  void delete() {
    userRepository.save(u1);
    assertThat(userService.delete(NAME_1), is(u1));
  }

  @Test
  void deleteFails() {
    assertThrows(NoSuchElementException.class, () -> userService.delete(NAME_1));
  }
}
