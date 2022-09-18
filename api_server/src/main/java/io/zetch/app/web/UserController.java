package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.User;
import io.zetch.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/users")
@Tag(name = "Users")
public class UserController {
    private final UserService userService;

    @Autowired
    UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(path="/")
    @Operation(summary = "Retrieve all users")
    @ResponseBody Iterable<User> getAllUsers() {
        return userService.getAll();
    }

    @PostMapping(path="/")
    @Operation(summary = "Create a new user")
    @ResponseBody String addNewUser(@RequestBody User newUser) {
        userService.createNew(newUser);
        return "User saved";
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retrieve a single user")
    User getOneUser(@PathVariable String username) {
        return userService.getOne(username);
    }

    @PutMapping("/{username}")
    @Operation(summary = "Modify user attributes")
    User updateUser(@RequestBody User newUser, @PathVariable String username) {
        return userService.update(newUser, username);
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