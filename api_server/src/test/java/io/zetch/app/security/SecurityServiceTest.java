package io.zetch.app.security;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

@ExtendWith(MockitoExtension.class)
class SecurityServiceTest {

  private final String USERNAME_1 = "bob";
  private final String USERNAME_2 = "admin";
  private final String CLIENT_1 = "some_client";
  private final String CLIENT_2 = "another_client";

  UserEntity bob = new UserEntity(USERNAME_1, Affiliation.STUDENT, "", "");
  UserEntity admin = new UserEntity(USERNAME_2, Affiliation.ADMIN, "", "");

  @Mock private UserRepository userRepository;
  @InjectMocks private SecurityService securityService;

  @Test
  void isSelf_NullToken() {
    assertThat(securityService.isSelf(null, USERNAME_1), is(false));
  }

  @Test
  void isAdmin_NullToken() {
    assertThat(securityService.isSelf(null, USERNAME_1), is(false));
  }

  @Test
  void isSelf() {
    assertThat(securityService.isSelf(getJwtForTest(USERNAME_1, CLIENT_1), USERNAME_1), is(true));
  }

  @Test
  void isSelf_Failure() {
    assertThat(securityService.isSelf(getJwtForTest(USERNAME_1, CLIENT_1), USERNAME_2), is(false));
  }

  @Test
  void isSelfClient() {
    assertThat(
        securityService.isSelfClient(getJwtForTest(USERNAME_1, CLIENT_1), CLIENT_1), is(true));
  }

  @Test
  void isSelfClient_Failure() {
    assertThat(
        securityService.isSelfClient(getJwtForTest(USERNAME_1, CLIENT_2), CLIENT_1), is(false));
  }

  @Test
  void isAdmin() {
    when(userRepository.findByUsername(USERNAME_2)).thenReturn(Optional.ofNullable(admin));
    assertThat(securityService.isAdmin(getJwtForTest(USERNAME_2, CLIENT_1)), is(true));
  }

  @Test
  void isAdmin_Failure() {
    when(userRepository.findByUsername(USERNAME_1)).thenReturn(Optional.ofNullable(bob));
    assertThat(securityService.isAdmin(getJwtForTest(USERNAME_1, CLIENT_1)), is(false));
  }

  private JwtAuthenticationToken getJwtForTest(String username, String clientId) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("username", username); // Only this matter, everything else is a placeholder
    claims.put("client_id", clientId); // Only this matter, everything else is a placeholder

    Map<String, Object> headers = new HashMap<>();
    headers.put("123", "123");

    Jwt temp = new Jwt("123", Instant.EPOCH, Instant.MAX, headers, claims);

    return new JwtAuthenticationToken(temp);
  }
}
