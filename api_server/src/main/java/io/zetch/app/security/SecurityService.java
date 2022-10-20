package io.zetch.app.security;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class SecurityService {
  private final UserRepository userRepository;

  @Autowired
  public SecurityService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /** Return True if the user from the provided token is an admin */
  public boolean isAdmin(JwtAuthenticationToken token) {
    if (token == null) return false;
    UserEntity user = getUserFromToken(token);
    return user.getAffiliation() == Affiliation.ADMIN;
  }

  /**
   * Return True if the user from the provided token is the same user that was passed in the path
   * variable
   */
  public boolean isSelf(JwtAuthenticationToken token, String pathUsername) {
    if (token == null) return false;
    return Objects.equals(getUsernameFromToken(token), pathUsername);
  }

  /**
   * Return True if the client ID from the provided token is the same client ID that was passed in
   */
  public boolean isSelfClient(JwtAuthenticationToken token, String clientId) {
    if (token == null) return false;
    return Objects.equals(getClientIdFromToken(token), clientId);
  }

  /** Return the UserEntity corresponding to the provided token */
  private UserEntity getUserFromToken(JwtAuthenticationToken token) {
    String username = token.getToken().getClaimAsString("username");
    return userRepository.findByUsername(username).orElseThrow();
  }

  /** Return the username from the provided token */
  private String getUsernameFromToken(JwtAuthenticationToken token) {
    return token.getToken().getClaimAsString("username");
  }

  /** Return the username from the provided token */
  private String getClientIdFromToken(JwtAuthenticationToken token) {
    return token.getToken().getClaimAsString("client_id");
  }
}
