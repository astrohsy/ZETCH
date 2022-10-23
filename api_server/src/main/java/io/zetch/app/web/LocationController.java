package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.location.LocationDto;
import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.service.LocationService;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
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

  /** Returns a list of all restraurants. */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all locations")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<LocationDto> getAllLocations(JwtAuthenticationToken token) {
    return locationService.getAll().stream().map(LocationEntity::toDto).toList();
  }

  /**
   * Returns a single location.
   *
   * @param name Location's name
   * @return A location by name
   */
  @GetMapping("/{name}")
  @Operation(summary = "Retrieve a single location")
  @SecurityRequirement(name = "OAuth2")
  LocationDto getOneLocation(@PathVariable String name, JwtAuthenticationToken token) {
    return locationService.getOne(name).toDto();
  }

  /**
   * Modifies a location.
   *
   * @param name Location's name
   * @return A location by name
   */
  @PutMapping("/{name}")
  @Operation(summary = "Modify a single location")
  @SecurityRequirement(name = "OAuth2")
  LocationDto updateLocation(
      @RequestBody LocationDto newLocationDto,
      @PathVariable String name,
      JwtAuthenticationToken token) {
    return locationService
        .update(name, newLocationDto.name(), newLocationDto.cuisine(), newLocationDto.address())
        .toDto();
  }

  /**
   * Updates location's owner.
   *
   * @param name Location's name
   * @param owner Owner's name
   * @return Updated Location DTO
   */
  @PutMapping("/{name}/{owner}")
  @Operation(summary = "Assign owner to a location")
  @SecurityRequirement(name = "OAuth2")
  LocationDto assignLocationOwner(
      @PathVariable String name, @PathVariable String owner, JwtAuthenticationToken token) {
    return locationService.assignOwner(name, owner).toDto();
  }

  /**
   * Creates a new location.
   *
   * @param locationDto Location data transfer object
   * @return Newly-created Location DTO
   */
  @PostMapping(path = "/")
  @Operation(summary = "Create a new location")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  LocationDto addNewLocation(
      @RequestBody @Validated LocationDto locationDto, JwtAuthenticationToken token) {
    return locationService
        .createNew(locationDto.name(), locationDto.cuisine(), locationDto.address())
        .toDto();
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
