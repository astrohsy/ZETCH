package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.c4_soft.springaddons.security.oauth2.test.annotations.Claims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.service.ReviewService;
import java.util.ArrayList;
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
@WithMockJwtAuth(
    claims =
        @OpenIdClaims(
            otherClaims =
                @Claims(stringClaims = @StringClaim(name = "username", value = "some_user"))))
class ReviewControllerTest {

  private static final String REVIEWS_ENDPOINT = "/reviews/";
  @Autowired ObjectMapper mapper;
  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean private ReviewService reviewServiceMock;
  static Gson gson = new Gson();

  @BeforeEach
  void setup() {
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
  }

  //  @Test
  //  void addNewUser() throws Exception {
  //    ReviewEntity r =
  //        reviewService.createNew(
  //            newReviewDto.getComment(),
  //            newReviewDto.getRating(),
  //            newReviewDto.getUser_id(),
  //            newReviewDto.getLocation_id());
  //    return g.fromJson(g.toJson(r), ReviewDto.class);
  //    mockMvc
  //        .perform(get(REVIEWS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
  //        .andExpect(status().isOk())
  //        .andExpect(jsonPath("*", notNullValue()))
  //        .andExpect(jsonPath("$", hasSize(2)));
  //  }

  @Test
  void getAllReviews() throws Exception {
    List<String> jsonReviews = new ArrayList<>();
    jsonReviews.add(
        """
           {
              "id": 0, "rating": 4, "comment": "Very tasty!",
              location: { id: 0, "name": "Bob's", "cuisine": "Italian", "address": "1234 Broadway" },
              user: { id: 0, "username": "bob", "name": "Bob", "email": "bob@example.com" }
           }
        """);
    jsonReviews.add(
        """
              {
                "id": 1, "rating": 1, "comment": "Terrible service.",
                location: { id: 0, "name": "Bob's", "cuisine": "Italian", "address": "1234 Broadway" },
                user: { id: 1, "username": "joe", "name": "Job", "email": "joe@example.com" }
               }
            """);

    List<ReviewEntity> reviews =
        jsonReviews.stream().map(x -> gson.fromJson(x, ReviewEntity.class)).toList();
    when(reviewServiceMock.getAll()).thenReturn(reviews);

    mockMvc
        .perform(get(REVIEWS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void getOneReview() throws Exception {
    List<String> jsonReviews = new ArrayList<>();
    jsonReviews.add(
        """
               {
                  "id": 0, "rating": 4, "comment": "Very tasty!",
                  location: { id: 0, "name": "Bob's", "cuisine": "Italian", "address": "1234 Broadway" },
                  user: { id: 0, "username": "bob", "name": "Bob", "email": "bob@example.com" }
               }
            """);
    jsonReviews.add(
        """
                  {
                    "id": 1, "rating": 1, "comment": "Terrible service.",
                    location: { id: 0, "name": "Bob's", "cuisine": "Italian", "address": "1234 Broadway" },
                    user: { id: 1, "username": "joe", "name": "Job", "email": "joe@example.com" }
                   }
                """);
    List<ReviewEntity> reviews =
        jsonReviews.stream().map(x -> gson.fromJson(x, ReviewEntity.class)).toList();
    when(reviewServiceMock.getOne(anyLong())).thenReturn(reviews.get(0));

    Long testReviewId = reviews.get(0).getId();
    mockMvc
        .perform(get(REVIEWS_ENDPOINT + testReviewId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()));
  }
}
