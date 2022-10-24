package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.user.UserDto;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.domain.user.UserGetDto;
import io.zetch.app.domain.user.UserPutDto;
import io.zetch.app.service.UserService;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/** Controller for the user endpoints. */
@RestController
@RequestMapping(path = "/users")
@Tag(name = "Users")
@CrossOrigin(origins = "*")
public class UserController {
  private final UserService userService;

  @Autowired
  UserController(UserService userService) {
    this.userService = userService;
  }

  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all users")
  @SecurityRequirement(name = "OAuth2")
  @PreAuthorize("@securityService.isAdmin(#token)")
  @ResponseBody
  Iterable<UserGetDto> getAllUsers(JwtAuthenticationToken token) {
    return userService.getAll().stream().map(UserEntity::toGetDto).toList();
  }

  @PostMapping(path = "/")
  @Operation(summary = "Create a new user")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  UserGetDto addNewUser(@RequestBody UserDto newUserDto) {
    return userService
        .createNew(
            newUserDto.username(), newUserDto.name(), newUserDto.email(), newUserDto.affiliation())
        .toGetDto();
  }

  @GetMapping("/{username}")
  @Operation(summary = "Retrieve a single user")
  @SecurityRequirement(name = "OAuth2")
  UserGetDto getOneUser(@PathVariable String username) {
    return userService.getOne(username).toGetDto();
  }

  @PutMapping("/{username}")
  @Operation(summary = "Modify user attributes")
  @SecurityRequirement(name = "OAuth2")
  @PreAuthorize("@securityService.isSelf(#token, #username)")
  UserGetDto updateUser(
      @RequestBody UserPutDto newUserDto,
      @PathVariable String username,
      JwtAuthenticationToken token) {
    return userService
        .update(username, newUserDto.name(), newUserDto.email(), newUserDto.affiliation())
        .toGetDto();
  }

  @DeleteMapping("/{username}")
  @PreAuthorize("@securityService.isSelf(#token, #username)")
  @SecurityRequirement(name = "OAuth2")
  @Operation(summary = "Delete a user")
  UserGetDto deleteUser(@PathVariable String username, JwtAuthenticationToken token) {
    return userService.delete(username).toGetDto();
  }

  /**
   * Returns 404 Not Found if NoSuchElementException is thrown in this Controller.
   *
   * @param ex Exception
   * @return Error message string
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  String return404(NoSuchElementException ex) {
    return ex.getMessage();
  }

  /**
   * Returns 400 Bad Request if IllegalArgumentException is thrown in this Controller.
   *
   * @param ex Exception
   * @return Error message string
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  String return404(IllegalArgumentException ex) {
    return ex.getMessage();
  }
}
