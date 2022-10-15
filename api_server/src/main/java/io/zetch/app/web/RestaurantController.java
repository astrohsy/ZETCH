package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.restaurant.RestaurantDto;
import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.service.RestaurantService;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/restaurants")
@Tag(name = "Restaurants")
@CrossOrigin(origins = "*")
public class RestaurantController {
  private final RestaurantService restaurantService;

  @Autowired
  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  /**
   * @return A list of all restraurants
   */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all restaurants")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<RestaurantDto> getAllRestaurants() {
    return restaurantService.getAll().stream()
        .map(RestaurantEntity::toDto)
        .collect(Collectors.toList());
  }

  /**
   * @param name Restaurant's name
   * @return A restaurant by name
   */
  @GetMapping("/{name}")
  @Operation(summary = "Retrieve a single restaurant")
  @SecurityRequirement(name = "OAuth2")
  RestaurantDto getOneRestaurant(@PathVariable String name) {
    return restaurantService.getOne(name).toDto();
  }

  /**
   * @param name Restaurant's name
   * @return A restaurant by name
   */
  @PutMapping("/{name}")
  @Operation(summary = "Modify a single restaurant")
  @SecurityRequirement(name = "OAuth2")
  RestaurantDto updateRestaurant(
      @RequestBody RestaurantDto newRestaurantDto, @PathVariable String name) {
    return restaurantService
        .update(
            name,
            newRestaurantDto.getName(),
            newRestaurantDto.getCuisine(),
            newRestaurantDto.getAddress())
        .toDto();
  }

  /**
   * @param name Restaurant's name
   * @param name Owner's name return Confirmation message if successful
   */
  @PutMapping("/{name}/{owner}")
  @Operation(summary = "Assign owner to a restaurant")
  @SecurityRequirement(name = "OAuth2")
  RestaurantDto assignRestaurantOwner(@PathVariable String name, @PathVariable String owner) {
    return restaurantService.assignOwner(name, owner).toDto();
  }

  /**
   * @param restaurantDto Restaurant data transfer object
   * @return Confirmation message if successful
   */
  @PostMapping(path = "/")
  @Operation(summary = "Create a new restaurant")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  RestaurantDto addNewRestaurant(@RequestBody @Validated RestaurantDto restaurantDto) {
    return restaurantService
        .createNew(restaurantDto.getName(), restaurantDto.getCuisine(), restaurantDto.getAddress())
        .toDto();
  }

  /**
   * Exception handler if NoSuchElementException is thrown in this Controller
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
   * Return 400 Bad Request if IllegalArgumentException is thrown in this Controller
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
