package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.restaurant.RestaurantDto;
import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.service.RestaurantService;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

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
  @ResponseBody
  Iterable<RestaurantDto> getAllRestaurants() {
    return restaurantService.getAll().stream().map(this::toDto).collect(Collectors.toList());
  }

  /**
   * @param restaurantId Restaurant's id
   * @return A restaurant by id
   */
  @GetMapping("/{restaurantId}")
  @Operation(summary = "Retrieve a single restaurant")
  RestaurantDto getOneRestaurant(@PathVariable Long restaurantId) {
    return toDto(restaurantService.getOne(restaurantId));
  }

  /**
   * @param restaurantDto Restaurant data transfer object
   * @return Confirmation message if successful
   */
  @PostMapping(path = "/")
  @Operation(summary = "Create a new restaurant")
  @ResponseBody
  RestaurantDto addNewRestaurant(@RequestBody @Validated RestaurantDto restaurantDto) {
    return toDto(
        restaurantService.createNew(
            restaurantDto.getName(), restaurantDto.getCuisine(), restaurantDto.getAddress()));
  }

  /**
   * Convert the Restaurant entity to a Restaurant data transfer object
   *
   * @param restaurant Restaurant to convert
   * @return Restaurant DTO
   */
  private RestaurantDto toDto(RestaurantEntity restaurant) {
    return new RestaurantDto(
            restaurant.getId(),
            restaurant.getName(),
            restaurant.getCuisine(),
            restaurant.getAddress());
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
}
