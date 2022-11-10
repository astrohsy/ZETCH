package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.location.LocationAvgRatingDto;
import io.zetch.app.domain.location.LocationDto;
import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.LocationGetDto;
import io.zetch.app.domain.location.LocationRatingHistogramDto;
import io.zetch.app.domain.location.LocationSearchDto;
import io.zetch.app.service.LocationService;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
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

/** Controller for the location endpoints. */
@RestController
@RequestMapping(path = "/locations")
@Tag(name = "Locations")
@CrossOrigin(origins = "*") // NOSONAR
public class LocationController {
  private final LocationService locationService;

  @Autowired
  public LocationController(LocationService locationService) {
    this.locationService = locationService;
  }

  /** Returns a list of all locations. */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all locations.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<LocationGetDto> getAllLocations(JwtAuthenticationToken token) {
    return locationService.getAll().stream().map(LocationEntity::toGetDto).toList();
  }

  /**
   * Returns a single location.
   *
   * @param name Location's name
   * @return A location by name
   */
  @GetMapping("/{name}")
  @Operation(summary = "Retrieve a single location.")
  @SecurityRequirement(name = "OAuth2")
  LocationGetDto getOneLocation(@PathVariable String name, JwtAuthenticationToken token) {
    return locationService.getOne(name).toGetDto();
  }

  /** Searches for a location. */
  @GetMapping("/search")
  @Operation(summary = "Search for locations.")
  @SecurityRequirement(name = "OAuth2")
  Iterable<LocationGetDto> searchLocation(
      LocationSearchDto searchParams, JwtAuthenticationToken token) {
    return locationService
        .search(searchParams.name(), searchParams.description(), searchParams.type())
        .stream()
        .map(LocationEntity::toGetDto)
        .toList();
  }

  /**
   * Updates a location.
   *
   * @param name Location's name
   * @return A location by name
   */
  @PutMapping("/{name}")
  @Operation(summary = "Modify a single location.")
  @SecurityRequirement(name = "OAuth2")
  LocationGetDto updateLocation(
      @RequestBody LocationDto newLocationDto,
      @PathVariable String name,
      JwtAuthenticationToken token) {
    return locationService
        .update(
            name,
            newLocationDto.name(),
            newLocationDto.description(),
            newLocationDto.address(),
            newLocationDto.type())
        .toGetDto();
  }

  /**
   * Assigns an owner to a location.
   *
   * @param name Location's name
   * @param owner Owner's name
   */
  @PutMapping("/{name}/{owner}")
  @Operation(summary = "Assign owner to a location.")
  @SecurityRequirement(name = "OAuth2")
  LocationGetDto assignLocationOwner(
      @PathVariable String name, @PathVariable String owner, JwtAuthenticationToken token) {
    return locationService.assignOwner(name, owner).toGetDto();
  }

  /**
   * Creates a location.
   *
   * @param locationDto Location data transfer object
   * @return Confirmation message if successful
   */
  @PostMapping(path = "/")
  @Operation(summary = "Create a new location.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  LocationGetDto addNewLocation(
      @RequestBody @Validated LocationDto locationDto, JwtAuthenticationToken token) {
    return locationService
        .createNew(
            locationDto.name(),
            locationDto.description(),
            locationDto.address(),
            locationDto.type())
        .toGetDto();
  }

  /**
   * Deletes a location.
   *
   * @param name Name of Location to delete
   * @return Confirmation message if successful
   */
  @DeleteMapping(path = "/{name}")
  @Operation(summary = "Delete a location.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  LocationDto deleteLocation(@PathVariable String name, JwtAuthenticationToken token) {
    return locationService.delete(name).toDto();
  }

  /**
   * Get location's rating histogram.
   *
   * @param name Name of Location to delete
   * @return Location's rating histogram.
   */
  @DeleteMapping(path = "/{name}/ratingHistogram")
  @Operation(summary = "Get location's rating histogram.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  LocationRatingHistogramDto getRatingHistogram(
      @PathVariable String name, JwtAuthenticationToken token) {
    return new LocationRatingHistogramDto(locationService.getRatingHistogram(name));
  }

  /* Returns an average rating for a location.
   *
   * @param name Location's name
   * @return Location's average rating.
   */
  @GetMapping("/{name}/averageRating")
  @Operation(summary = "Retrieve the average rating of a location.")
  @SecurityRequirement(name = "OAuth2")
  LocationAvgRatingDto getAverageRating(@PathVariable String name, JwtAuthenticationToken token) {
    return new LocationAvgRatingDto(locationService.averageRating(name));
  }

  /**
   * Exception handler if NoSuchElementException is thrown in this Controller.
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
   * Return 400 Bad Request if IllegalArgumentException is thrown in this Controller.
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
