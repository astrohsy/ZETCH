package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserDto;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.service.UserService;
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
public class UserControllerTest {

  private static final String USERS_ENDPOINT = "/users/";

  private static final String USERNAME_1 = "bob";
  private static final String NAME_1 = "Bob";
  private static final String EMAIL_1 = "bob@example.com";
  private static final String USERNAME_2 = "cat";
  private static final String NAME_2 = "Cat";
  private static final String EMAIL_2 = "cat@example.com";

  private MockMvc mockMvc;
  @Autowired ObjectMapper mapper;
  @Autowired private WebApplicationContext context;
  @MockBean private UserService userServiceMock;

  UserEntity u1 =
      UserEntity.builder()
          .username(USERNAME_1)
          .displayName(NAME_1)
          .email(EMAIL_1)
          .affiliation(Affiliation.STUDENT)
          .build();
  UserEntity u2 =
      UserEntity.builder()
          .username(USERNAME_2)
          .displayName(NAME_2)
          .email(EMAIL_2)
          .affiliation(Affiliation.STUDENT)
          .build();

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  public void getAllUsers() throws Exception {
    when(userServiceMock.getAll()).thenReturn(List.of(u1, u2));

    mockMvc
        .perform(get(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(2)))
        .andExpect(jsonPath("$[0].username", is(USERNAME_1)))
        .andExpect(jsonPath("$[1].username", is(USERNAME_2)));
  }

  @Test
  public void getUserById() throws Exception {
    when(userServiceMock.getOne(u1.getUsername())).thenReturn(u1);

    mockMvc
        .perform(get(USERS_ENDPOINT + u1.getUsername()).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.username", is(u1.getUsername())))
        .andExpect(jsonPath("$.name", is(u1.getDisplayName())))
        .andExpect(jsonPath("$.email", is(u1.getEmail())));
  }

  @Test
  public void createUser() throws Exception {
    when(userServiceMock.createNew(
            u1.getUsername(), u1.getDisplayName(), u1.getEmail(), u1.getAffiliation().toString()))
        .thenReturn(u1);

    MockHttpServletRequestBuilder mockRequest =
        post(USERS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(u1.toDto()));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.username", is(u1.getUsername())))
        .andExpect(jsonPath("$.name", is(u1.getDisplayName())))
        .andExpect(jsonPath("$.email", is(u1.getEmail())));
  }

  @Test
  public void createUser_UnavailableUsername() throws Exception {
    when(userServiceMock.createNew(any(), any(), any(), any()))
        .thenThrow(IllegalArgumentException.class);

    MockHttpServletRequestBuilder mockRequest =
        post(USERS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(u1));

    mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
  }

  @Test
  public void updateUserAttrs() throws Exception {
    UserEntity updated =
        UserEntity.builder()
            .username(USERNAME_1)
            .displayName("Bob New")
            .email("bob_new@me.com")
            .affiliation(Affiliation.FACULTY)
            .build();

    when(userServiceMock.update(
            u1.getUsername(),
            updated.getDisplayName(),
            updated.getEmail(),
            updated.getAffiliation().toString()))
        .thenReturn(updated);

    MockHttpServletRequestBuilder mockRequest =
        put(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updated.toDto()));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.username", is(u1.getUsername())))
        .andExpect(jsonPath("$.name", is(updated.getDisplayName())))
        .andExpect(jsonPath("$.email", is(updated.getEmail())))
        .andExpect(jsonPath("$.affiliation", is(updated.getAffiliation().toString())));
  }

  @Test
  public void updateUser_UserNotFound() throws Exception {
    UserEntity updated =
        UserEntity.builder()
            .username(USERNAME_1)
            .displayName("Bob New")
            .email("bob_new@me.com")
            .affiliation(Affiliation.FACULTY)
            .build();

    when(userServiceMock.update(
            u1.getUsername(),
            updated.getDisplayName(),
            updated.getEmail(),
            updated.getAffiliation().toString()))
        .thenThrow(NoSuchElementException.class);

    MockHttpServletRequestBuilder mockRequest =
        put(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updated.toDto()));

    mockMvc.perform(mockRequest).andExpect(status().isNotFound());
  }

  @Test
  public void updateUser_InvalidAffiliation() throws Exception {
    when(userServiceMock.update(USERNAME_1, NAME_1, EMAIL_1, "badAffiliation"))
        .thenThrow(IllegalArgumentException.class);

    MockHttpServletRequestBuilder mockRequest =
        put(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new UserDto(USERNAME_1, NAME_1, EMAIL_1, "badAffiliation")));

    mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
  }

  @Test
  public void deleteUser() throws Exception {
    when(userServiceMock.delete(u1.getUsername())).thenReturn(u1);

    MockHttpServletRequestBuilder mockRequest =
        delete(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.username", is(u1.getUsername())))
        .andExpect(jsonPath("$.name", is(u1.getDisplayName())))
        .andExpect(jsonPath("$.email", is(u1.getEmail())))
        .andExpect(jsonPath("$.affiliation", is(u1.getAffiliation().toString())));
  }

  @Test
  public void deleteUser_UserNotFound() throws Exception {
    when(userServiceMock.delete(u1.getUsername())).thenThrow(NoSuchElementException.class);

    MockHttpServletRequestBuilder mockRequest =
        delete(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(mockRequest).andExpect(status().isNotFound());
  }
}
