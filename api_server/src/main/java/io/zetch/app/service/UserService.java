package io.zetch.app.service;

import io.zetch.app.domain.User;
import io.zetch.app.repo.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
   * Retrieve all users
   *
   * @return List of all users
   */
  public List<User> getAll() {
    return userRepository.findAll();
  }

  /**
   * Retrieve one user
   *
   * @param username User's username
   * @return User
   */
  public User getOne(String username) {
    return verifyUser(username);
  }

  /**
   * Create a new User in the database and Cognito
   *
   * @param username User's username
   * @param name User's name
   * @param email User's email
   * @return User
   */
  public User createNew(String username, String name, String email) {
    if (userRepository.existsById(username)) {
      throw new IllegalArgumentException("Username unavailable: " + username);
    }

    // Add user to Cognito
    cognitoService.signUp(username);

    User newUser = new User(username, name, email);
    return userRepository.save(newUser);
  }

  /**
   * Update existing User with any non-null attributes. Changing username is not supported.
   *
   * @param currUsername Username of User to be updated
   * @param newName New name
   * @param newEmail New email
   * @return Updated User object
   * @throws NoSuchElementException If User not found
   */
  public User update(String currUsername, String newName, String newEmail)
      throws NoSuchElementException {
    User currUser = verifyUser(currUsername);

    // TODO: Maybe there is a better way to set
    if (newName != null) {
      currUser.setName(newName);
    }

    if (newEmail != null) {
      currUser.setEmail(newEmail);
    }

    return userRepository.save(currUser);
  }

  /**
   * Verify and return the User for a particular username
   *
   * @param username Username to find
   * @return Found User
   * @throws NoSuchElementException If User not found
   */
  public User verifyUser(String username) throws NoSuchElementException {
    return userRepository
        .findById(username)
        .orElseThrow(() -> new NoSuchElementException("User does not exist: " + username));
  }
}
