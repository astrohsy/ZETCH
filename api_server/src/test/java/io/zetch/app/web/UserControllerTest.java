package io.zetch.app.web;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.domain.User;
import io.zetch.app.domain.UserDto;
import io.zetch.app.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.List;
import java.util.NoSuchElementException;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final String USERS_ENDPOINT = "/users/";

    private static final String USERNAME_1 = "bob";
    private static final String NAME_1 = "Bob";
    private static final String EMAIL_1 = "bob@example.com";
    private static final String USERNAME_2 = "cat";
    private static final String NAME_2 = "Cat";
    private static final String EMAIL_2 = "cat@example.com";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    @MockBean
    private UserService userServiceMock;

    User u1 = new User(USERNAME_1, NAME_1, EMAIL_1);
    User u2 = new User(USERNAME_2, NAME_2, EMAIL_2);

    @Test
    public void getAllUsers() throws Exception {
        when(userServiceMock.getAll()).thenReturn(List.of(u1, u2));

        mockMvc.perform(get(USERS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].username", is(USERNAME_1)))
                .andExpect(jsonPath("$[1].username", is(USERNAME_2)));
    }

    @Test
    public void getUserById() throws Exception {
        when(userServiceMock.getOne(u1.getUsername())).thenReturn(u1);

        mockMvc.perform(get(USERS_ENDPOINT + u1.getUsername()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$.username", is(u1.getUsername())))
                .andExpect(jsonPath("$.name", is(u1.getName())))
                .andExpect(jsonPath("$.email", is(u1.getEmail())));

    }

    @Test
    public void createUser() throws Exception {
        when(userServiceMock.createNew(u1.getUsername(), u1.getName(), u1.getEmail())).thenReturn(u1);

        MockHttpServletRequestBuilder mockRequest = post(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new UserDto(u1.getUsername(), u1.getName(), u1.getEmail())));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$.username", is(u1.getUsername())))
                .andExpect(jsonPath("$.name", is(u1.getName())))
                .andExpect(jsonPath("$.email", is(u1.getEmail())));
    }

    @Test
    public void createUser_UnavailableUsername() throws Exception {
        when(userServiceMock.createNew(any(), any(), any())).thenThrow(IllegalArgumentException.class);

        MockHttpServletRequestBuilder mockRequest = post(USERS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(u1));

        mockMvc.perform(mockRequest).andExpect(status().isBadRequest());
    }

    @Test
    public void updateUserName() throws Exception {
        User updated = new User(USERNAME_1, "New Bob", EMAIL_1);
        when(userServiceMock.update(u1.getUsername(), updated.getName(), null)).thenReturn(updated);

        MockHttpServletRequestBuilder mockRequest = put(USERS_ENDPOINT + USERNAME_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new UserDto(USERNAME_1, "New Bob", null)));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$.username", is(u1.getUsername())))
                .andExpect(jsonPath("$.name", is(updated.getName())))
                .andExpect(jsonPath("$.email", is(u1.getEmail())));
    }

    @Test
    public void updateUserEmail() throws Exception {
        User updated = new User(USERNAME_1, NAME_1, "new_bob@me.com");
        when(userServiceMock.update(u1.getUsername(), null, updated.getEmail())).thenReturn(updated);

        MockHttpServletRequestBuilder mockRequest = put(USERS_ENDPOINT + USERNAME_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new UserDto(USERNAME_1, null, "new_bob@me.com")));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$.username", is(u1.getUsername())))
                .andExpect(jsonPath("$.name", is(u1.getName())))
                .andExpect(jsonPath("$.email", is(updated.getEmail())));
    }

    @Test
    public void updateUserNameAndEmail() throws Exception {
        User updated = new User(USERNAME_1, "Bob New", "bob_new@me.com");
        when(userServiceMock.update(u1.getUsername(), updated.getName(), updated.getEmail())).thenReturn(updated);

        MockHttpServletRequestBuilder mockRequest = put(USERS_ENDPOINT + USERNAME_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new UserDto(USERNAME_1, "Bob New", "bob_new@me.com")));

        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("*", notNullValue()))
                .andExpect(jsonPath("$.username", is(u1.getUsername())))
                .andExpect(jsonPath("$.name", is(updated.getName())))
                .andExpect(jsonPath("$.email", is(updated.getEmail())));
    }

    @Test
    public void updateUser_UserNotFound() throws Exception {
        User updated = new User(USERNAME_1, "Bob New", "bob_new@me.com");
        when(userServiceMock.update(u1.getUsername(), updated.getName(), updated.getEmail())).thenThrow(NoSuchElementException.class);

        MockHttpServletRequestBuilder mockRequest = put(USERS_ENDPOINT + USERNAME_1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(new UserDto(USERNAME_1, "Bob New", "bob_new@me.com")));

        mockMvc.perform(mockRequest).andExpect(status().isNotFound());
    }
}
