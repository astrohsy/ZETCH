package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

  private static final String NAME = "Bob's";
  private static final String NAME_2 = "Sally's";
  private static final String USER_NAME = "Bob";
  private static final String CUISINE = "Italian";
  private static final String ADDRESS = "1234 Broadway";

  @Mock private LocationRepository locationRepositoryMock;
  @Mock private UserRepository userRepositoryMock;
  @InjectMocks private LocationService locationService;
  @Mock private LocationEntity locationMock;
  @Mock private UserEntity userMock;

  // VERIFY SERVICE RETURN VALUE

  @Test
  void getOne() {
    when(locationRepositoryMock.findByName(NAME)).thenReturn(Optional.of(locationMock));
    assertThat(locationService.getOne(NAME), is(locationMock));
  }

  @Test
  void getOneFails() {
    when(locationRepositoryMock.findByName(NAME)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> locationService.getOne(NAME));
  }

  @Test
  void getAll() {
    when(locationRepositoryMock.findAll())
        .thenReturn(List.of(locationMock, locationMock, locationMock));
    assertThat(locationService.getAll().size(), is(3));
    assertThat(locationService.getAll().get(0), is(locationMock));
  }

  // VERIFY INVOCATION OF DEPS + PARAMETERS

  @Test
  void createNew() {
    // Prepare to capture a Location object
    ArgumentCaptor<LocationEntity> locationCaptor = ArgumentCaptor.forClass(LocationEntity.class);

    locationService.createNew(NAME, CUISINE, ADDRESS);

    // Verify save() invoked
    verify(locationRepositoryMock).save(locationCaptor.capture());

    // Verify the attributes of the Location object
    LocationEntity value = locationCaptor.getValue();
    assertThat(value.getName(), is(NAME));
    assertThat(value.getCuisine(), is(CUISINE));
    assertThat(value.getAddress(), is(ADDRESS));
    assertThat(value.getOwners().isEmpty(), is(true));
  }

  @Test
  void createNewUnavailable() {
    when(locationRepositoryMock.existsByName(NAME)).thenReturn(true);
    assertThrows(
        IllegalArgumentException.class, () -> locationService.createNew(NAME, CUISINE, ADDRESS));
  }

  @Test
  void updateLocationName() throws Exception {
    LocationEntity old =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name(NAME)
            .cuisine(CUISINE)
            .address(ADDRESS)
            .build();

    LocationEntity updated =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name("New Bob's")
            .cuisine(CUISINE)
            .address(ADDRESS)
            .build();

    when(locationRepositoryMock.findByName(NAME)).thenReturn(Optional.of(old));
    locationService.update(NAME, updated.getName(), updated.getCuisine(), updated.getAddress());

    ArgumentCaptor<LocationEntity> locationCaptor = ArgumentCaptor.forClass(LocationEntity.class);
    verify(locationRepositoryMock).save(locationCaptor.capture());

    LocationEntity value = locationCaptor.getValue();
    assertThat(value.getName(), is(updated.getName()));
    assertThat(value.getCuisine(), is(updated.getCuisine()));
    assertThat(value.getAddress(), is(updated.getAddress()));
    assertThat(value.getOwners().isEmpty(), is(true));
  }

  @Test
  void updateLocationUnavailable() {
    when(locationRepositoryMock.findByName(NAME)).thenReturn(Optional.of(locationMock));
    when(locationRepositoryMock.existsByName(NAME_2)).thenReturn(true);
    assertThrows(
        IllegalArgumentException.class,
        () -> locationService.update(NAME, NAME_2, CUISINE, ADDRESS));
  }

  @Test
  void assignOwner() {

    LocationEntity location = LocationEntity.builder().owners(new ArrayList<>()).name(NAME).build();

    UserEntity user =
        UserEntity.builder().ownedLocations(new ArrayList<>()).username("user").build();

    when(locationRepositoryMock.findByName(NAME)).thenReturn(Optional.of(location));
    when(userRepositoryMock.findByUsername(USER_NAME)).thenReturn(Optional.of(user));

    locationService.assignOwner(NAME, USER_NAME);

    assertThat(location.getOwners().get(0), is(user));
    assertThat(user.getOwnedLocations().get(0), is(location));
  }
}
