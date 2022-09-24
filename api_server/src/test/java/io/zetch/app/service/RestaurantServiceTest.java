package io.zetch.app.service;

import io.zetch.app.domain.Restaurant;
import io.zetch.app.repo.RestaurantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

  private static final Long ID = 1L;
  private static final String NAME = "Bob's";
  private static final String CUISINE = "Italian";
  private static final String ADDRESS = "1234 Broadway";

  @Mock private RestaurantRepository restaurantRepositoryMock;
  @InjectMocks private RestaurantService restaurantService;
  @Mock private Restaurant restaurantMock;

  // VERIFY SERVICE RETURN VALUE

  @Test
  public void getOne() {
    when(restaurantRepositoryMock.findById(ID)).thenReturn(Optional.of(restaurantMock));
    assertThat(restaurantService.getOne(ID), is(restaurantMock));
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
    ArgumentCaptor<Restaurant> restaurantCaptor = ArgumentCaptor.forClass(Restaurant.class);

    restaurantService.createNew(NAME, CUISINE, ADDRESS);

    // Verify save() invoked
    verify(restaurantRepositoryMock).save(restaurantCaptor.capture());

    // Verify the attributes of the Restaurant object
    Restaurant value = restaurantCaptor.getValue();
    assertThat(value.getName(), is(NAME));
    assertThat(value.getCuisine(), is(CUISINE));
    assertThat(value.getAddress(), is(ADDRESS));
    assertThat(value.getOwners().isEmpty(), is(true));
  }
}
