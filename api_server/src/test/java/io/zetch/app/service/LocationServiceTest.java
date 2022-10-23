package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.UserRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

  private static final String NAME = "Bob's";
  private static final String NAME_2 = "Sally's";
  private static final String USER_NAME = "Bob";
  private static final String DESCRIPTION = "Italian";
  private static final String ADDRESS = "1234 Broadway";
  private static final String TYPE = "restaurant";

  @Mock private LocationRepository locationRepositoryMock;
  @Mock private UserRepository userRepositoryMock;
  @InjectMocks private LocationService locationService;
  @Mock private LocationEntity locationMock;

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

  @Test
  void search() {
    when(locationRepositoryMock.findByNameAndType(NAME, Type.fromString(TYPE)))
        .thenReturn(List.of(locationMock, locationMock, locationMock));
    assertThat(locationService.search(NAME, TYPE).size(), is(3));
    assertThat(locationService.search(NAME, TYPE).get(0), is(locationMock));
  }

  // VERIFY INVOCATION OF DEPS + PARAMETERS

  @Test
  void createNew() {
    // Prepare to capture a Location object
    ArgumentCaptor<LocationEntity> locationCaptor = ArgumentCaptor.forClass(LocationEntity.class);

    locationService.createNew(NAME, DESCRIPTION, ADDRESS, TYPE);

    // Verify save() invoked
    verify(locationRepositoryMock).save(locationCaptor.capture());

    // Verify the attributes of the Location object
    LocationEntity value = locationCaptor.getValue();
    assertThat(value.getName(), is(NAME));
    assertThat(value.getDescription(), is(DESCRIPTION));
    assertThat(value.getAddress(), is(ADDRESS));
    assertThat(value.getOwners().isEmpty(), is(true));
    assertThat(value.getType(), is(Type.fromString(TYPE)));
  }

  @Test
  void createNewUnavailable() {
    when(locationRepositoryMock.existsByName(NAME)).thenReturn(true);
    assertThrows(
        IllegalArgumentException.class,
        () -> locationService.createNew(NAME, DESCRIPTION, ADDRESS, TYPE));
  }

  @ParameterizedTest
  @CsvSource(
      value = {
        "New Name, null, null, null",
        "null, New Desc, null, null",
        "null, null, New Addr, null",
        "null, null, null, museum",
        "New Name, New Desc, New Addr, museum"
      },
      nullValues = {"null"})
  void updateLocation(String newName, String newDesc, String newAddr, String newType) {
    LocationEntity old =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name(NAME)
            .description(DESCRIPTION)
            .address(ADDRESS)
            .type(Type.fromString("restaurant"))
            .build();

    LocationEntity updated =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name(newName != null ? newName : NAME)
            .description(newDesc != null ? newDesc : DESCRIPTION)
            .address(newAddr != null ? newAddr : ADDRESS)
            .type(newType != null ? Type.fromString(newType) : Type.fromString("restaurant"))
            .build();

    when(locationRepositoryMock.findByName(NAME)).thenReturn(Optional.of(old));
    locationService.update(NAME, newName, newDesc, newAddr, newType);

    ArgumentCaptor<LocationEntity> locationCaptor = ArgumentCaptor.forClass(LocationEntity.class);
    verify(locationRepositoryMock).save(locationCaptor.capture());

    LocationEntity value = locationCaptor.getValue();
    assertThat(value.getName(), is(updated.getName()));
    assertThat(value.getDescription(), is(updated.getDescription()));
    assertThat(value.getAddress(), is(updated.getAddress()));
    assertThat(value.getType(), is(updated.getType()));
    assertThat(value.getOwners().isEmpty(), is(true));
  }

  @Test
  void updateLocationUnavailable() {
    when(locationRepositoryMock.findByName(NAME)).thenReturn(Optional.of(locationMock));
    when(locationRepositoryMock.existsByName(NAME_2)).thenReturn(true);
    assertThrows(
        IllegalArgumentException.class,
        () -> locationService.update(NAME, NAME_2, DESCRIPTION, ADDRESS, TYPE));
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
