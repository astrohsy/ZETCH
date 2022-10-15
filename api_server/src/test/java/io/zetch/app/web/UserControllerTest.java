package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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
import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserDto;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.security.SecurityService;
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
@WithMockJwtAuth(
    claims =
        @OpenIdClaims(
            otherClaims =
                @Claims(stringClaims = @StringClaim(name = "username", value = "some_user"))))
class UserControllerTest {

  private static final String USERS_ENDPOINT = "/users/";

  private static final String USERNAME_1 = "bob";
  private static final String NAME_1 = "Bob";
  private static final String EMAIL_1 = "bob@example.com";
  private static final String USERNAME_2 = "cat";
  private static final String NAME_2 = "Cat";
  private static final String EMAIL_2 = "cat@example.com";
  @Autowired ObjectMapper mapper;
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
  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean private UserService userServiceMock;
  @MockBean private SecurityService securityServiceMock;

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = "admin"))))
  void getAllUsers_ByAdmin() throws Exception {
    when(securityServiceMock.isAdmin(any())).thenReturn(true);
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
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_1))))
  void getAllUsers_NotByAdmin() throws Exception {
    when(securityServiceMock.isAdmin(any())).thenReturn(false);
    when(userServiceMock.getAll()).thenReturn(List.of(u1, u2));

    mockMvc
        .perform(get(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void getUserById() throws Exception {
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
  void createUser() throws Exception {
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
  void createUser_UnavailableUsername() throws Exception {
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
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_1))))
  void updateUserAttrs() throws Exception {
    when(securityServiceMock.isSelf(any(), eq(USERNAME_1))).thenReturn(true);

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
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_2))))
  void updateUserAttrs_NotSelf() throws Exception {
    UserEntity updated =
        UserEntity.builder()
            .username(USERNAME_1)
            .displayName("Bob New")
            .email("bob_new@me.com")
            .affiliation(Affiliation.FACULTY)
            .build();

    MockHttpServletRequestBuilder mockRequest =
        put(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(mapper.writeValueAsString(updated.toDto()));

    mockMvc.perform(mockRequest).andExpect(status().isForbidden());
  }

  @Test
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_1))))
  void updateUser_UserNotFound() throws Exception {
    when(securityServiceMock.isSelf(any(), eq(USERNAME_1))).thenReturn(true);

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
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_1))))
  void updateUser_InvalidAffiliation() throws Exception {
    when(securityServiceMock.isSelf(any(), eq(USERNAME_1))).thenReturn(true);
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
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_1))))
  void deleteUser() throws Exception {
    when(securityServiceMock.isSelf(any(), eq(USERNAME_1))).thenReturn(true);
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
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_2))))
  void deleteUser_NotSelf() throws Exception {
    when(securityServiceMock.isSelf(any(), eq(USERNAME_2))).thenReturn(false);
    when(userServiceMock.delete(u1.getUsername())).thenReturn(u1);

    MockHttpServletRequestBuilder mockRequest =
        delete(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(mockRequest).andExpect(status().isForbidden());
  }

  @Test
  @WithMockJwtAuth(
      claims =
          @OpenIdClaims(
              otherClaims =
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_1))))
  void deleteUser_UserNotFound() throws Exception {
    when(securityServiceMock.isSelf(any(), eq(USERNAME_1))).thenReturn(true);
    when(userServiceMock.delete(USERNAME_1)).thenThrow(NoSuchElementException.class);

    MockHttpServletRequestBuilder mockRequest =
        delete(USERS_ENDPOINT + USERNAME_1)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON);

    mockMvc.perform(mockRequest).andExpect(status().isNotFound());
  }
}
