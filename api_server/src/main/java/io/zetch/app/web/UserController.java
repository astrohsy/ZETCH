package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.Restaurant;
import io.zetch.app.domain.User;
import io.zetch.app.domain.UserDto;
import io.zetch.app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
    @ResponseBody Iterable<UserDto> getAllUsers() {
        return userService.getAll().stream().map(this::toDto).collect(Collectors.toList());
    }

    @PostMapping(path="/")
    @Operation(summary = "Create a new user")
    @ResponseBody String addNewUser(@RequestBody UserDto newUserDto) {
        userService.createNew(newUserDto.getUsername(), newUserDto.getName(), newUserDto.getEmail());
        return "User saved";
    }

    @GetMapping("/{username}")
    @Operation(summary = "Retrieve a single user")
    UserDto getOneUser(@PathVariable String username) {
        return toDto(userService.getOne(username));
    }

    @PutMapping("/{username}")
    @Operation(summary = "Modify user attributes")
    UserDto updateUser(@RequestBody UserDto newUserDto, @PathVariable String username) {
        return toDto(userService.update(username, newUserDto.getName(), newUserDto.getEmail()));
    }

    /**
     * Convert the User entity to a User data transfer object
     *
     * @param user User to convert
     * @return User DTO
     */
    private UserDto toDto(User user) {
        return new UserDto(user.getUsername(), user.getName(), user.getEmail());
    }

    /**
     * Return 404 Not Found if NoSuchElementException is thrown in this Controller
     * @param ex Exception
     * @return Error message string
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementException.class)
    String return404(NoSuchElementException ex) {
        return ex.getMessage();
    }

    /**
     * Return 400 Bad Request if IllegalArgumentException is thrown in this Controller
     * @param ex Exception
     * @return Error message string
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    String return404(IllegalArgumentException ex) {
        return ex.getMessage();
    }
}
