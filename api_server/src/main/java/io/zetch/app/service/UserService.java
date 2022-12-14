package io.zetch.app.service;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import io.zetch.app.security.CognitoService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Service providing User business logic. */
@Service
public class UserService {
  private final UserRepository userRepository;
  private final CognitoService cognitoService;

  @Autowired
  public UserService(UserRepository userRepository, CognitoService cognitoService) {
    this.userRepository = userRepository;
    this.cognitoService = cognitoService;
  }

  /**
   * Retrieves all users.
   *
   * @return List of all users
   */
  public List<UserEntity> getAll() {
    return userRepository.findAll();
  }

  /**
   * Retrieves one user.
   *
   * @param username User's username
   * @return User
   */
  public UserEntity getOne(String username) {
    return verifyUser(username);
  }

  /**
   * Creates a new User in the database and Cognito.
   *
   * @param username User's username
   * @param name User's name
   * @param email User's email
   * @return User
   * @throws IllegalArgumentException If username unavailable or invalid Affiliation passed
   */
  public UserEntity createNew(
      String username, String name, String email, String affiliation, String clientId)
      throws IllegalArgumentException {
    if (userRepository.existsByUsername(username)) {
      throw new IllegalArgumentException("Username unavailable: " + username);
    }

    // Add user to Cognito
    cognitoService.signUp(username, clientId);

    UserEntity newUser =
        UserEntity.builder()
            .username(username)
            .displayName(name)
            .email(email)
            .affiliation(Affiliation.fromString(affiliation))
            .ownedLocations(new ArrayList<>())
            .build();

    return userRepository.save(newUser);
  }

  /**
   * Updates existing User with any non-null attributes. Changing username is not supported.
   *
   * @param currUsername Username of User to be updated
   * @param newName New name
   * @param newEmail New email
   * @param affiliation User affiliation
   * @return Updated User object
   * @throws NoSuchElementException If User not found
   * @throws IllegalArgumentException If invalid Affiliation passed
   */
  public UserEntity update(String currUsername, String newName, String newEmail, String affiliation)
      throws NoSuchElementException, IllegalArgumentException {
    UserEntity currUser = verifyUser(currUsername);

    if (newName != null) {
      currUser.setDisplayName(newName);
    }

    if (newEmail != null) {
      currUser.setEmail(newEmail);
    }

    if (affiliation != null) {
      currUser.setAffiliation(Affiliation.fromString(affiliation));
    }

    return userRepository.save(currUser);
  }

  /**
   * Deletes a User from the database.
   *
   * @param username Username of User to delete
   * @return User that was just deleted
   * @throws NoSuchElementException If User not found
   */
  public UserEntity delete(String username) throws NoSuchElementException {
    UserEntity user = verifyUser(username);

    userRepository.delete(user);

    return user;
  }

  /**
   * Verifies and returns the User for a particular username.
   *
   * @param username Username to find
   * @return Found User
   * @throws NoSuchElementException If User not found
   */
  public UserEntity verifyUser(String username) throws NoSuchElementException {
    return userRepository
        .findByUsernameIgnoreCase(username)
        .orElseThrow(() -> new NoSuchElementException("User does not exist: " + username));
  }
}
