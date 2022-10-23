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
import org.springframework.web.bind.annotation.*;

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

  /**
   * @return A list of all restraurants
   */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all locations")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<LocationDto> getAllLocations(JwtAuthenticationToken token) {
    return locationService.getAll().stream().map(LocationEntity::toDto).toList();
  }

  /**
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
   * @param name Location's name
   * @return A location by name
   */
  @GetMapping("/{name}/{type}")
  @Operation(summary = "Search locations by name and type")
  @SecurityRequirement(name = "OAuth2")
  Iterable<LocationDto> searchLocation(
      @PathVariable String name, @PathVariable String type, JwtAuthenticationToken token) {
    return locationService.search(name, type).stream().map(LocationEntity::toDto).toList();
  }

  /**
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
        .update(
            name,
            newLocationDto.name(),
            newLocationDto.description(),
            newLocationDto.address(),
            newLocationDto.type())
        .toDto();
  }

  /**
   * @param name Location's name
   * @param name Owner's name return Confirmation message if successful
   */
  @PutMapping("/{name}/{owner}")
  @Operation(summary = "Assign owner to a location")
  @SecurityRequirement(name = "OAuth2")
  LocationDto assignLocationOwner(
      @PathVariable String name, @PathVariable String owner, JwtAuthenticationToken token) {
    return locationService.assignOwner(name, owner).toDto();
  }

  /**
   * @param locationDto Location data transfer object
   * @return Confirmation message if successful
   */
  @PostMapping(path = "/")
  @Operation(summary = "Create a new location")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  LocationDto addNewLocation(
      @RequestBody @Validated LocationDto locationDto, JwtAuthenticationToken token) {
    return locationService
        .createNew(
            locationDto.name(),
            locationDto.description(),
            locationDto.address(),
            locationDto.type())
        .toDto();
  }

  /**
   * @param name Name of Location to delete
   * @return Confirmation message if successful
   */
  @DeleteMapping(path = "/{name}")
  @Operation(summary = "Delete a location")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  LocationDto deleteLocation(@PathVariable String name, JwtAuthenticationToken token) {
    return locationService.delete(name).toDto();
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
