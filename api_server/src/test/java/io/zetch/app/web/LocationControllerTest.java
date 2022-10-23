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

import com.c4_soft.springaddons.security.oauth2.test.annotations.Claims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.domain.location.LocationDto;
import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.service.LocationService;

import java.util.*;

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
@WithMockJwtAuth(
    claims =
        @OpenIdClaims(
            otherClaims =
                @Claims(stringClaims = @StringClaim(name = "username", value = "some_user"))))
class LocationControllerTest {

  private static final String LOCATION_ENDPOINT = "/locations/";
  private static final String NAME_1 = "Bob's";
  private static final String DESCRIPTION_1 = "Italian";
  private static final String ADDRESS_1 = "1234 Broadway";
  private static final String TYPE_1 = "museum";
  private static final String NAME_2 = "Cat's";
  private static final String DESCRIPTION_2 = "French";
  private static final String ADDRESS_2 = "15 Amsterdam";
  private static final String TYPE_2 = "building";
  private static final String USERNAME_1 = "Bob";
  @Autowired ObjectMapper mapper;
  LocationEntity r1 =
      LocationEntity.builder()
          .name(NAME_1)
          .description(DESCRIPTION_1)
          .address(ADDRESS_1)
          .type(Type.fromString(TYPE_1))
          .build();

  LocationEntity r2 =
      LocationEntity.builder()
          .name(NAME_2)
          .description(DESCRIPTION_2)
          .address(ADDRESS_2)
          .type(Type.fromString(TYPE_2))
          .build();

  UserEntity u1 =
      UserEntity.builder()
          .username(USERNAME_1)
          .displayName(USERNAME_1)
          .email(null)
          .affiliation(Affiliation.STUDENT)
          .build();
  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean private LocationService locationServiceMock;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  void getAllLocations() throws Exception {
    when(locationServiceMock.getAll()).thenReturn(List.of(r1, r2));

    mockMvc
        .perform(get(LOCATION_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].*", hasSize(5)))
        .andExpect(jsonPath("$[0].name", is(NAME_1)))
        .andExpect(jsonPath("$[1].name", is(NAME_2)));
  }

  @Test
  void getLocationByName() throws Exception {
    when(locationServiceMock.getOne(NAME_1)).thenReturn(r1);

    mockMvc
        .perform(get(LOCATION_ENDPOINT + NAME_1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.*", hasSize(5)))
        .andExpect(jsonPath("$.name", is(r1.getName())))
        .andExpect(jsonPath("$.description", is(r1.getDescription())))
        .andExpect(jsonPath("$.address", is(r1.getAddress())))
        .andExpect(jsonPath("$.type", is(TYPE_1)));
  }

  @Test
  void createLocation() throws Exception {
    when(locationServiceMock.createNew(r1.getName(), r1.getDescription(), r1.getAddress(), TYPE_1))
        .thenReturn(r1);

    MockHttpServletRequestBuilder mockRequest =
        post(LOCATION_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new LocationDto(r1.getName(), r1.getDescription(), r1.getAddress(), TYPE_1)));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.name", is(r1.getName())))
        .andExpect(jsonPath("$.description", is(r1.getDescription())))
        .andExpect(jsonPath("$.address", is(r1.getAddress())))
        .andExpect(jsonPath("$.type", is(TYPE_1)));
  }

  @Test
  void updateLocationName() throws Exception {
    LocationEntity updated =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name("New Bob's")
            .description(DESCRIPTION_1)
            .address(ADDRESS_1)
            .type(Type.fromString(TYPE_1))
            .build();
    when(locationServiceMock.update(NAME_1, updated.getName(), null, null, null))
        .thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
        put(LOCATION_ENDPOINT + NAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(new LocationDto("New Bob's", null, null, null)));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.name", is(updated.getName())))
        .andExpect(jsonPath("$.description", is(updated.getDescription())))
        .andExpect(jsonPath("$.address", is(updated.getAddress())))
        .andExpect(jsonPath("$.type", is(TYPE_1)));
  }

  @Test
  void updateLocationDescription() throws Exception {
    LocationEntity updated =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name(NAME_1)
            .description("New Italian")
            .address(ADDRESS_1)
            .type(Type.fromString(TYPE_1))
            .build();
    when(locationServiceMock.update(NAME_1, null, updated.getDescription(), null, null))
        .thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
        put(LOCATION_ENDPOINT + NAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(new LocationDto(null, "New Italian", null, null)));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.name", is(updated.getName())))
        .andExpect(jsonPath("$.description", is(updated.getDescription())))
        .andExpect(jsonPath("$.address", is(updated.getAddress())))
        .andExpect(jsonPath("$.type", is(TYPE_1)));
  }

  @Test
  void updateLocationAddress() throws Exception {
    LocationEntity updated =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name(NAME_1)
            .description(DESCRIPTION_1)
            .address("New 1234 Broadway")
            .type(Type.fromString(TYPE_1))
            .build();
    when(locationServiceMock.update(NAME_1, null, null, "New 1234 Broadway", null))
        .thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
        put(LOCATION_ENDPOINT + NAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(new LocationDto(null, null, "New 1234 Broadway", null)));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.name", is(updated.getName())))
        .andExpect(jsonPath("$.description", is(updated.getDescription())))
        .andExpect(jsonPath("$.address", is(updated.getAddress())))
        .andExpect(jsonPath("$.type", is(TYPE_1)));
  }

  @Test
  void updateUserNotFound() throws Exception {
    LocationEntity updated =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name("New Bob's")
            .description("New Italian")
            .address("New 1234 Broadway")
            .build();
    when(locationServiceMock.update(
            NAME_1, updated.getName(), updated.getDescription(), updated.getAddress(), TYPE_1))
        .thenThrow(NoSuchElementException.class);

    MockHttpServletRequestBuilder mockRequest =
        put(LOCATION_ENDPOINT + NAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new LocationDto("New Bob's", "New Italian", "New 1234 Broadway", TYPE_1)));

    mockMvc.perform(mockRequest).andExpect(status().isNotFound());
  }

  @Test
  void updateIllegalArgument() throws Exception {
    LocationEntity updated =
        LocationEntity.builder()
            .owners(new ArrayList<>())
            .name("New Bob's")
            .description("New Italian")
            .address("New 1234 Broadway")
            .build();
    when(locationServiceMock.update(
            NAME_1, updated.getName(), updated.getDescription(), updated.getAddress(), TYPE_1))
        .thenThrow(IllegalArgumentException.class);

    MockHttpServletRequestBuilder mockRequest =
        put(LOCATION_ENDPOINT + NAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new LocationDto("New Bob's", "New Italian", "New 1234 Broadway", TYPE_1)));

    mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
  }

  @Test
  void search() throws Exception {
    when(locationServiceMock.search(NAME_1, TYPE_1)).thenReturn(Arrays.asList(r1));

    mockMvc
        .perform(
            get(LOCATION_ENDPOINT + NAME_1 + '/' + TYPE_1).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].*", hasSize(5)))
        .andExpect(jsonPath("$[0].name", is(r1.getName())))
        .andExpect(jsonPath("$[0].description", is(r1.getDescription())))
        .andExpect(jsonPath("$[0].address", is(r1.getAddress())))
        .andExpect(jsonPath("$[0].type", is(TYPE_1)));
  }

  @Test
  void assignOwner() throws Exception {
    LocationEntity assigned =
        LocationEntity.builder()
            .owners(Arrays.asList(u1))
            .name("New Bob's")
            .description(DESCRIPTION_1)
            .address(ADDRESS_1)
            .type(Type.fromString(TYPE_1))
            .build();
    when(locationServiceMock.assignOwner(NAME_1, USERNAME_1)).thenReturn(assigned);

    MockHttpServletRequestBuilder mockRequest =
        put(LOCATION_ENDPOINT + NAME_1 + '/' + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new LocationDto("New Bob's", DESCRIPTION_1, ADDRESS_1, TYPE_1)));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.name", is(assigned.getName())))
        .andExpect(jsonPath("$.description", is(assigned.getDescription())))
        .andExpect(jsonPath("$.address", is(assigned.getAddress())))
        .andExpect(jsonPath("$.type", is(TYPE_1)));
  }
}
