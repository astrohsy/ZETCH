package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.domain.restaurant.RestaurantDto;
import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.service.RestaurantService;

import java.util.ArrayList;
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
  @Autowired ObjectMapper mapper;
  RestaurantEntity r1 =
      RestaurantEntity.builder().name(NAME_1).cuisine(CUISINE_1).address(ADDRESS_1).build();

  RestaurantEntity r2 =
      RestaurantEntity.builder().name(NAME_2).cuisine(CUISINE_2).address(ADDRESS_2).build();
  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean private RestaurantService restaurantServiceMock;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

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
    when(restaurantServiceMock.getOne(ID_1)).thenReturn(r1);

    mockMvc
        .perform(get(RESTAURANT_ENDPOINT + ID_1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.*", hasSize(4)))
        .andExpect(jsonPath("$.id", is(r1.getId())))
        .andExpect(jsonPath("$.name", is(r1.getName())))
        .andExpect(jsonPath("$.cuisine", is(r1.getCuisine())))
        .andExpect(jsonPath("$.address", is(r1.getAddress())));
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
                    RestaurantDto.builder()
                        .id(r1.getId())
                        .name(r1.getName())
                        .cuisine(r1.getCuisine())
                        .address(r1.getAddress())
                        .build()));

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
    RestaurantEntity updated =
            RestaurantEntity.builder()
                    .owners(new ArrayList<>())
                    .name("New Bob's")
                    .cuisine(CUISINE_1)
                    .address(ADDRESS_1)
                    .build();
    when(restaurantServiceMock.update(ID_1, updated.getName(), null, null)).thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + ID_1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(
                            RestaurantDto.builder()
                                    .id(ID_1)
                                    .name("New Bob's")
                                    .cuisine(null)
                                    .address(null)
                                    .build()));

    mockMvc
            .perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("*", notNullValue()))
            .andExpect(jsonPath("$.name", is(updated.getName())))
            .andExpect(jsonPath("$.cuisine", is(updated.getCuisine())))
            .andExpect(jsonPath("$.address", is(updated.getAddress())));
  }

  @Test
  public void updateRestaurantCuisine() throws Exception {
    RestaurantEntity updated =
            RestaurantEntity.builder()
                    .owners(new ArrayList<>())
                    .name(NAME_1)
                    .cuisine("New Italian")
                    .address(ADDRESS_1)
                    .build();
    when(restaurantServiceMock.update(ID_1, null, updated.getCuisine(), null)).thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + ID_1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(
                            RestaurantDto.builder()
                                    .id(ID_1)
                                    .name(null)
                                    .cuisine("New Italian")
                                    .address(null)
                                    .build()));

    mockMvc
            .perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("*", notNullValue()))
            .andExpect(jsonPath("$.name", is(updated.getName())))
            .andExpect(jsonPath("$.cuisine", is(updated.getCuisine())))
            .andExpect(jsonPath("$.address", is(updated.getAddress())));
  }

  @Test
  public void updateRestaurantAddress() throws Exception {
    RestaurantEntity updated =
            RestaurantEntity.builder()
                    .owners(new ArrayList<>())
                    .name(NAME_1)
                    .cuisine(CUISINE_1)
                    .address("New 1234 Broadway")
                    .build();
    when(restaurantServiceMock.update(ID_1, null, null, "New 1234 Broadway")).thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + ID_1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(
                            RestaurantDto.builder()
                                    .id(ID_1)
                                    .name(null)
                                    .cuisine(null)
                                    .address("New 1234 Broadway")
                                    .build()));

    mockMvc
            .perform(mockRequest)
            .andExpect(status().isOk())
            .andExpect(jsonPath("*", notNullValue()))
            .andExpect(jsonPath("$.name", is(updated.getName())))
            .andExpect(jsonPath("$.cuisine", is(updated.getCuisine())))
            .andExpect(jsonPath("$.address", is(updated.getAddress())));
  }

  @Test
  public void updateUserNotFound() throws Exception {
    RestaurantEntity updated =
            RestaurantEntity.builder()
                    .owners(new ArrayList<>())
                    .name("New Bob's")
                    .cuisine("New Italian")
                    .address("New 1234 Broadway")
                    .build();
    when(restaurantServiceMock.update(ID_1, updated.getName(), updated.getCuisine(), updated.getAddress())).thenThrow(NoSuchElementException.class);

    MockHttpServletRequestBuilder mockRequest =
            put(RESTAURANT_ENDPOINT + ID_1)
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(
                            RestaurantDto.builder()
                                    .id(ID_1)
                                    .name("New Bob's")
                                    .cuisine("New Italian")
                                    .address("New 1234 Broadway")
                                    .build()));

    mockMvc
            .perform(mockRequest)
            .andExpect(status().isNotFound());
  }
}
