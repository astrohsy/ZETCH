package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.domain.Restaurant;
import io.zetch.app.domain.RestaurantDto;
import io.zetch.app.service.RestaurantService;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class RestaurantControllerTest {

  private static final String RESTAURANT_ENDPOINT = "/restaurants/";

  private static final Long ID_1 = 1L;
  private static final String NAME_1 = "Bob's";
  private static final String CUISINE_1 = "Italian";
  private static final String ADDRESS_1 = "1234 Broadway";
  private static final Long ID_2 = 2L;
  private static final String NAME_2 = "Cat's";
  private static final String CUISINE_2 = "French";
  private static final String ADDRESS_2 = "15 Amsterdam";

  private MockMvc mockMvc;

  @Autowired private WebApplicationContext context;

  @Autowired ObjectMapper mapper;

  @MockBean private RestaurantService restaurantServiceMock;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  Restaurant r1 = new Restaurant(ID_1, NAME_1, CUISINE_1, ADDRESS_1);
  Restaurant r2 = new Restaurant(ID_2, NAME_2, CUISINE_2, ADDRESS_2);

  @Test
  public void getAllRestaurants() throws Exception {
    when(restaurantServiceMock.getAll()).thenReturn(List.of(r1, r2));

    mockMvc
        .perform(get(RESTAURANT_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].*", hasSize(4)))
        .andExpect(jsonPath("$[0].name", is(NAME_1)))
        .andExpect(jsonPath("$[1].name", is(NAME_2)));
  }

  @Test
  public void getRestaurantById() throws Exception {
    when(restaurantServiceMock.getOne(r1.getId())).thenReturn(r1);

    mockMvc
        .perform(get(RESTAURANT_ENDPOINT + r1.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.*", hasSize(4)))
        .andExpect(jsonPath("$.id", is(r1.getId().intValue())))
        .andExpect(jsonPath("$.name", is(r1.getName())))
        .andExpect(jsonPath("$.cuisine", is(r1.getCuisine())))
        .andExpect(jsonPath("$.address", is(r1.getAddress())));
  }

  @Test
  public void getRestaurantByIdFails() throws Exception {
    when(restaurantServiceMock.getOne(r1.getId())).thenThrow(new NoSuchElementException("Restaurant does not exist: " + r1.getId()));

    mockMvc
        .perform(get(RESTAURANT_ENDPOINT + r1.getId()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound());
  }

  @Test
  public void createRestaurant() throws Exception {
    when(restaurantServiceMock.createNew(r1.getName(), r1.getCuisine(), r1.getAddress()))
        .thenReturn(r1);

    MockHttpServletRequestBuilder mockRequest =
        post(RESTAURANT_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new RestaurantDto(r1.getId(), r1.getName(), r1.getCuisine(), r1.getAddress())));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.name", is(r1.getName())))
        .andExpect(jsonPath("$.cuisine", is(r1.getCuisine())))
        .andExpect(jsonPath("$.address", is(r1.getAddress())));
  }

  @Test
  public void updateRestaurantName() throws Exception {
    Restaurant updated = new Restaurant(ID_1, "New Bob's", CUISINE_1, ADDRESS_1);
    when(restaurantServiceMock.update(r1.getId(), updated.getName(), null, null)).thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + r1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(new RestaurantDto(r1.getId(), "New Bob's", null, null)));

    mockMvc
            .perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("*", notNullValue()))
            .andExpect(jsonPath("$.id", is(updated.getId().intValue())))
            .andExpect(jsonPath("$.name", is(updated.getName())))
            .andExpect(jsonPath("$.cuisine", is(updated.getCuisine())))
            .andExpect(jsonPath("$.address", is(updated.getAddress())));
  }

  @Test
  public void updateRestaurantCuisine() throws Exception {
    Restaurant updated = new Restaurant(ID_1, NAME_1, "New Italian", ADDRESS_1);
    when(restaurantServiceMock.update(r1.getId(), null, updated.getCuisine(), null)).thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + r1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(new RestaurantDto(r1.getId(), null, updated.getCuisine(), null)));

    mockMvc
            .perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("*", notNullValue()))
            .andExpect(jsonPath("$.id", is(updated.getId().intValue())))
            .andExpect(jsonPath("$.name", is(updated.getName())))
            .andExpect(jsonPath("$.cuisine", is(updated.getCuisine())))
            .andExpect(jsonPath("$.address", is(updated.getAddress())));
  }

  @Test
  public void updateRestaurantAddress() throws Exception {
    Restaurant updated = new Restaurant(ID_1, NAME_1, CUISINE_1, "New 1234 Broadway");
    when(restaurantServiceMock.update(r1.getId(), null, null, updated.getAddress())).thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + r1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(new RestaurantDto(r1.getId(), null, null, updated.getAddress())));

    mockMvc
            .perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("*", notNullValue()))
            .andExpect(jsonPath("$.id", is(updated.getId().intValue())))
            .andExpect(jsonPath("$.name", is(updated.getName())))
            .andExpect(jsonPath("$.cuisine", is(updated.getCuisine())))
            .andExpect(jsonPath("$.address", is(updated.getAddress())));
  }

  @Test
  public void updateUserNotFound() throws Exception {
    Restaurant updated = new Restaurant(ID_1, "New Bob's", "New Italian", "New 1234 Broadway");
    when(restaurantServiceMock.update(r1.getId(), updated.getName(), updated.getCuisine(), updated.getAddress()))
            .thenThrow(NoSuchElementException.class);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + r1.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(new RestaurantDto(r1.getId(), "New Bob's",
                            "New Italian", "New 1234 Broadway")));

    mockMvc.perform(mockRequest).andExpect(status().isNotFound());
  }
}
