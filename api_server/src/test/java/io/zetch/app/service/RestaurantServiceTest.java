package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.RestaurantRepository;
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
public class RestaurantServiceTest {

  private static final Long ID = 1L;
  private static final String NAME = "Bob's";
  private static final String NAME_2 = "Sally's";
  private static final String USER_NAME = "Bob";
  private static final String CUISINE = "Italian";
  private static final String ADDRESS = "1234 Broadway";

  @Mock private RestaurantRepository restaurantRepositoryMock;
  @Mock private UserRepository userRepositoryMock;
  @InjectMocks private RestaurantService restaurantService;
  @Mock private RestaurantEntity restaurantMock;
  @Mock private UserEntity userMock;

  // VERIFY SERVICE RETURN VALUE

  @Test
  public void getOne() {
    when(restaurantRepositoryMock.findByName(NAME)).thenReturn(Optional.of(restaurantMock));
    assertThat(restaurantService.getOne(NAME), is(restaurantMock));
  }

  @Test
  public void getOneFails() {
    when(restaurantRepositoryMock.findByName(NAME)).thenReturn(Optional.empty());
    assertThrows(NoSuchElementException.class, () -> restaurantService.getOne(NAME));
  }

  @Test
  public void getAll() {
    when(restaurantRepositoryMock.findAll())
        .thenReturn(List.of(restaurantMock, restaurantMock, restaurantMock));
    assertThat(restaurantService.getAll().size(), is(3));
    assertThat(restaurantService.getAll().get(0), is(restaurantMock));
  }

  // VERIFY INVOCATION OF DEPS + PARAMETERS

  @Test
  public void createNew() {
    // Prepare to capture a Restaurant object
    ArgumentCaptor<RestaurantEntity> restaurantCaptor =
        ArgumentCaptor.forClass(RestaurantEntity.class);

    restaurantService.createNew(NAME, CUISINE, ADDRESS);

    // Verify save() invoked
    verify(restaurantRepositoryMock).save(restaurantCaptor.capture());

    // Verify the attributes of the Restaurant object
    RestaurantEntity value = restaurantCaptor.getValue();
    assertThat(value.getName(), is(NAME));
    assertThat(value.getCuisine(), is(CUISINE));
    assertThat(value.getAddress(), is(ADDRESS));
    assertThat(value.getOwners().isEmpty(), is(true));
  }

  @Test
  public void createNewUnavailable() {
    when(restaurantRepositoryMock.existsByName(NAME)).thenReturn(true);
    assertThrows(
        IllegalArgumentException.class, () -> restaurantService.createNew(NAME, CUISINE, ADDRESS));
  }

  @Test
  public void updateRestaurantName() throws Exception {
    RestaurantEntity old =
        RestaurantEntity.builder()
            .owners(new ArrayList<>())
            .name(NAME)
            .cuisine(CUISINE)
            .address(ADDRESS)
            .build();

    RestaurantEntity updated =
        RestaurantEntity.builder()
            .owners(new ArrayList<>())
            .name("New Bob's")
            .cuisine(CUISINE)
            .address(ADDRESS)
            .build();

    when(restaurantRepositoryMock.findByName(NAME)).thenReturn(Optional.of(old));
    restaurantService.update(NAME, updated.getName(), updated.getCuisine(), updated.getAddress());

    ArgumentCaptor<RestaurantEntity> restaurantCaptor =
        ArgumentCaptor.forClass(RestaurantEntity.class);
    verify(restaurantRepositoryMock).save(restaurantCaptor.capture());

    RestaurantEntity value = restaurantCaptor.getValue();
    assertThat(value.getName(), is(updated.getName()));
    assertThat(value.getCuisine(), is(updated.getCuisine()));
    assertThat(value.getAddress(), is(updated.getAddress()));
    assertThat(value.getOwners().isEmpty(), is(true));
  }

  @Test
  public void updateRestaurantUnavailable() {
    when(restaurantRepositoryMock.findByName(NAME)).thenReturn(Optional.of(restaurantMock));
    when(restaurantRepositoryMock.existsByName(NAME_2)).thenReturn(true);
    assertThrows(
        IllegalArgumentException.class,
        () -> restaurantService.update(NAME, NAME_2, CUISINE, ADDRESS));
  }

  @Test
  public void assignOwner() {
    when(restaurantRepositoryMock.findByName(NAME)).thenReturn(Optional.of(restaurantMock));
    when(userRepositoryMock.findByUsername(USER_NAME)).thenReturn(Optional.of(userMock));
    restaurantService.assignOwner(NAME, USER_NAME);
  }
}
