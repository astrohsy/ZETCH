package com.example.demo.web;

import com.example.demo.domain.ZetchUser;
import com.example.demo.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Aggregate root
    @GetMapping(path="/")
    public @ResponseBody Iterable<ZetchUser> getAllUsers() {
        // This returns a JSON or XML with the users
        return userRepository.findAll();
    }

    @PostMapping(path="/")
    public @ResponseBody String addNewUser(@RequestBody ZetchUser newUser) {
        userRepository.save(newUser);
        return "Saved";
    }

    // Single item
    @GetMapping("/{username}")
    ZetchUser getOne(@PathVariable String username) {
        return userRepository.findById(username)
                .orElseThrow(() -> new NoSuchElementException("User does not exist: " + username));
    }

    @PatchMapping("/{username}")
    ZetchUser updateUser(@RequestBody ZetchUser newUser, @PathVariable String username) {
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
    public String return404(NoSuchElementException ex) {
        return ex.getMessage();
    }
}