package io.zetch.app.service;

import io.zetch.app.domain.User;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
     * Create a new User in the database
     *
     * @param newUser New User object
     */
    public void createNew(User newUser) {
        userRepository.save(newUser);
    }

    /**
     * Update existing User with any non-null attributes. Changing username is not supported.
     *
     * @param newUser User object with new attributes
     * @param username Username of User to be updated
     * @return Updated User object
     * @throws NoSuchElementException If User not found
     */
    public User update(User newUser, String username) throws NoSuchElementException {
        User currUser = verifyUser(username);

        // TODO: Maybe there is a better way to check for null and set
        String name = newUser.getName();
        if (name != null) {
            currUser.setName(name);
        }

        String email = newUser.getEmail();
        if (email != null) {
            currUser.setEmail(email);
        }

        return userRepository.save(currUser);
    }

    /**
     * Verify and return the User for a particular username
     * @param username Username to find
     * @return Found User
     * @throws NoSuchElementException If User not found
     */
    private User verifyUser(String username) throws NoSuchElementException {
        return userRepository.findById(username).orElseThrow(
                () -> new NoSuchElementException("User does not exist: " + username));
    }
}
