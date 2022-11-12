package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.hamcrest.core.Is.is;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class UserServiceIntegrationTest {

  @Autowired private UserService userService;
  @Autowired private UserRepository userRepository;

  @AfterEach
  void resetDatabase() {
    userRepository.deleteAll();
  }

  @Test
  void getAll() {
    UserEntity u1 =
        UserEntity.builder()
            .username("cat")
            .displayName("Cat")
            .email("cat@cat.cat")
            .affiliation(Affiliation.OTHER)
            .build();
    userRepository.save(u1);

    List<UserEntity> users = userService.getAll();
    assertThat(users.size(), is(1));
    assertThat(users.get(0), is(u1));
  }

  @Test
  void create() {
    UserEntity newUser = userService.createNew("bob", "bob", "bob@me.com", "other");
    List<UserEntity> users = userService.getAll();
    assertThat(users.size(), is(1));
    assertThat(newUser.getUsername(), is("bob"));
    assertThat(newUser.getDisplayName(), is("bob"));
    assertThat(newUser.getEmail(), is("bob@me.com"));
    assertThat(newUser.getAffiliation(), is(Affiliation.OTHER));
  }
}
