package io.zetch.app.service;

import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UserService {
  private final UserRepository userRepository;

  @Autowired
  public UserService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Retrieve all users
   *
   * @return List of all users
   */
  public List<UserEntity> getAll() {
    return userRepository.findAll();
  }

  /**
   * Retrieve one user
   *
   * @param username User's username
   * @return User
   */
  public UserEntity getOne(String username) {
    return verifyUser(username);
  }

  /** Create a new User in the database */
  public UserEntity createNew(String username, String name, String email) {
    if (userRepository.existsById(username)) {
      throw new IllegalArgumentException("Username unavailable: " + username);
    }

    UserEntity newUser =  UserEntity.builder()
            .username(username)
            .name(name)
            .email(email)
            .ownedRestaurants(new ArrayList<>())
            .build();
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
  public UserEntity update(String currUsername, String newName, String newEmail)
      throws NoSuchElementException {
    UserEntity currUser = verifyUser(currUsername);

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
  public UserEntity verifyUser(String username) throws NoSuchElementException {
    return userRepository
        .findById(username)
        .orElseThrow(() -> new NoSuchElementException("User does not exist: " + username));
  }
}
