package io.zetch.app.web;

import io.zetch.app.domain.User;
import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Aggregate root
    @GetMapping(path="/")
    @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @PostMapping(path="/")
    @ResponseBody String addNewUser(@RequestBody User newUser) {
        userRepository.save(newUser);
        return "Saved";
    }

    // Single item
    @GetMapping("/{username}")
    User getOne(@PathVariable String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("User does not exist: " + username));
    }

    @PatchMapping("/{username}")
    User updateUser(@RequestBody User newUser, @PathVariable String username) {
        return userRepository.findById(username).map(user -> {
            user.setName(newUser.getName());
            user.setEmail(newUser.getEmail());
            return userRepository.save(user);
        }).orElseThrow(() -> new NoSuchElementException("User does not exist: " + username));
    }

    /**
     * Exception handler if NoSuchElementException is thrown in this Controller
     * @param ex Exception
     * @return Error message string
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    String return404(NoSuchElementException ex) {
        return ex.getMessage();
    }
}