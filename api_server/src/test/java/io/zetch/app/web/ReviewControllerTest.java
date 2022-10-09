package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.service.ReviewService;
import java.util.List;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ContextConfiguration
public class ReviewControllerTest {

  private static final String REVIEWS_ENDPOINT = "/reviews/";
  @Autowired ObjectMapper mapper;
  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean private ReviewService reviewServiceMock;

  @BeforeEach
  public void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  @Test
  public void getAllReviews() throws Exception {
    when(reviewServiceMock.getAll()).thenReturn(List.of());

    mockMvc
        .perform(get(REVIEWS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(2)));
  }
  //
  //  @Test
  //  public void getUserById() throws Exception {
  //    when(userServiceMock.getOne(u1.getUsername())).thenReturn(u1);
  //
  //    mockMvc
  //        .perform(get(USERS_ENDPOINT + u1.getUsername()).contentType(MediaType.APPLICATION_JSON))
  //        .andExpect(status().isOk())
  //        .andExpect(jsonPath("*", notNullValue()))
  //        .andExpect(jsonPath("$.username", is(u1.getUsername())))
  //        .andExpect(jsonPath("$.name", is(u1.getName())))
  //        .andExpect(jsonPath("$.email", is(u1.getEmail())));
  //  }
  //  @Test
  //
  //  public void createUser() throws Exception {
  //    when(userServiceMock.createNew(u1.getUsername(), u1.getName(),
  // u1.getEmail())).thenReturn(u1);
  //
  //    MockHttpServletRequestBuilder mockRequest =
  //        post(USERS_ENDPOINT)
  //            .contentType(MediaType.APPLICATION_JSON)
  //            .accept(MediaType.APPLICATION_JSON)
  //            .content(
  //                mapper.writeValueAsString(
  //                    new UserDto(u1.getId(), u1.getUsername(), u1.getName(), u1.getEmail())));
  //
  //    mockMvc
  //        .perform(mockRequest)
  //        .andExpect(status().isOk())
  //        .andExpect(jsonPath("*", notNullValue()))
  //        .andExpect(jsonPath("$.username", is(u1.getUsername())))
  //        .andExpect(jsonPath("$.name", is(u1.getName())))
  //        .andExpect(jsonPath("$.email", is(u1.getEmail())));
  //  }
}
