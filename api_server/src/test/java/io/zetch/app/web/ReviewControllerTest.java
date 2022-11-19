package io.zetch.app.web;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
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
import io.zetch.app.domain.review.ReviewPostDto;
import io.zetch.app.security.SecurityService;
import io.zetch.app.service.ReviewService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

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
class ReviewControllerTest {

  private static final String REVIEWS_ENDPOINT = "/reviews/";
  @Autowired ObjectMapper mapper;
  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean private ReviewService reviewServiceMock;
  @MockBean private SecurityService securityServiceMock;
  private List<ReviewEntity> reviews;
  static Gson gson = new Gson();

  @BeforeEach
  void setup() {
    List<String> jsonReviews;
    mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
    jsonReviews = new ArrayList<>();
    jsonReviews.add(
        """
                   {
                      "id": 0, "rating": 4, "comment": "Very tasty!",
                      location: { id: 0, "name": "Bob's", "description": "Italian", "address": "1234 Broadway", "type": "museum" },
                      user: { id: 0, "username": "bob", "name": "Bob", "email": "bob@example.com", "affiliation": "other" }
                   }
                """);
    jsonReviews.add(
        """
                      {
                        "id": 1, "rating": 1, "comment": "Terrible service.",
                        location: { id: 1, "name": "Tom's", "description": "Italian", "address": "1234 Broadway", "type": "museum" },
                        user: { id: 1, "username": "joe", "name": "Job", "email": "joe@example.com", "affiliation": "other" }
                       }
                    """);
    reviews = jsonReviews.stream().map(x -> gson.fromJson(x, ReviewEntity.class)).toList();
  }

  @Test
  void createReview() throws Exception {
    ReviewEntity r1 = reviews.get(0);

    when(reviewServiceMock.createNew(
            r1.getComment(), r1.getRating(), r1.getUser().getId(), r1.getLocation().getId()))
        .thenReturn(r1);

    MockHttpServletRequestBuilder mockRequest =
        post(REVIEWS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new ReviewPostDto(
                        r1.getComment(),
                        r1.getRating(),
                        r1.getUser().getId(),
                        r1.getLocation().getId())));

    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.comment", is(r1.getComment())))
        .andExpect(jsonPath("$.rating", is(r1.getRating())));
    // .andExpect(jsonPath("$.user", is(g.toJson(r1.getUser()))))
    // .andExpect(jsonPath("$.location", is(g.toJson(r1.getLocation())));
  }

  @Test
  void getAllReviews() throws Exception {
    when(reviewServiceMock.getAll(Optional.empty(), Optional.empty())).thenReturn(reviews);
    mockMvc
        .perform(get(REVIEWS_ENDPOINT).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(2)));
  }

  @Test
  void getAllReviews_withLocationId() throws Exception {
    when(reviewServiceMock.getAll(Optional.of(1L), Optional.empty()))
        .thenReturn(reviews.subList(1, 2));
    mockMvc
        .perform(
            get(REVIEWS_ENDPOINT)
                .queryParam("locationId", "1")
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$", hasSize(1)));
  }

  @Test
  void getOneReview() throws Exception {
    when(reviewServiceMock.getOne(anyLong())).thenReturn(reviews.get(0));

    Long testReviewId = reviews.get(0).getId();
    mockMvc
        .perform(get(REVIEWS_ENDPOINT + testReviewId).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()));
  }

  @Test
  void deleteOneReview() throws Exception {
    doThrow(NoSuchElementException.class).when(reviewServiceMock).deleteOne(2L);
    when(securityServiceMock.isOwnedReview(any(), eq(1L))).thenReturn(true);
    when(securityServiceMock.isOwnedReview(any(), eq(2L))).thenReturn(false);

    mockMvc
        .perform(delete(REVIEWS_ENDPOINT + 1L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNoContent());

    mockMvc
        .perform(delete(REVIEWS_ENDPOINT + 2L).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isForbidden());
  }

  @Test
  void crate_raise_badRequest_when_invalid_rating() throws Exception {
    ReviewEntity r1 = reviews.get(0);

    when(reviewServiceMock.createNew(
            r1.getComment(), r1.getRating(), r1.getUser().getId(), r1.getLocation().getId()))
        .thenReturn(r1);

    MockHttpServletRequestBuilder mockRequestWithRatingBiggerThan5 =
        post(REVIEWS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new ReviewPostDto(
                        r1.getComment(), 6, r1.getUser().getId(), r1.getLocation().getId())));

    mockMvc.perform(mockRequestWithRatingBiggerThan5).andExpect(status().isBadRequest());

    MockHttpServletRequestBuilder mockRequestWithRatingSmallerThan1 =
        post(REVIEWS_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(
                mapper.writeValueAsString(
                    new ReviewPostDto(
                        r1.getComment(), 0, r1.getUser().getId(), r1.getLocation().getId())));

    mockMvc.perform(mockRequestWithRatingSmallerThan1).andExpect(status().isBadRequest());
  }
}
