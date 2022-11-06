package io.zetch.app.web;

import static io.zetch.app.TestConstants.REPLY_1;
import static io.zetch.app.TestConstants.REPLY_2;
import static io.zetch.app.TestConstants.REPLY_3;
import static io.zetch.app.TestConstants.REPLY_ID_1;
import static io.zetch.app.TestConstants.REPLY_ID_2;
import static io.zetch.app.TestConstants.REVIEW_ID_1;
import static io.zetch.app.TestConstants.USER_ID_1;
import static io.zetch.app.TestConstants.USERNAME_1;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.c4_soft.springaddons.security.oauth2.test.annotations.Claims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.OpenIdClaims;
import com.c4_soft.springaddons.security.oauth2.test.annotations.StringClaim;
import com.c4_soft.springaddons.security.oauth2.test.annotations.WithMockJwtAuth;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zetch.app.domain.reply.ReplyEntity;
import io.zetch.app.domain.reply.ReplyPostDto;
import io.zetch.app.security.SecurityService;
import io.zetch.app.service.ReplyService;
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
class ReplyControllerTest {
  private static final String REPLIES_ENDPOINT = "/replies/";
  private static final String USER_ROUTE = "user/";
  private static final String REVIEW_ROUTE = "review/";
  @Autowired ObjectMapper mapper;
  private MockMvc mockMvc;
  @Autowired private WebApplicationContext context;
  @MockBean private ReplyService replyServiceMock;
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
                  @Claims(stringClaims = @StringClaim(name = "username", value = USERNAME_1))))
  void addNewReply_createsSuccessfully() throws Exception {
    // Arrange
    ReplyEntity expected = REPLY_1;
    ReplyPostDto reply =
        new ReplyPostDto(
            expected.getReplyComment(),
            expected.getReplyUser().getId(),
            expected.getReview().getId());

    when(securityServiceMock.isSelfPostReply(any(), any())).thenReturn(true);
    when(replyServiceMock.createNew(
            expected.getReplyComment(),
            expected.getReplyUser().getId(),
            expected.getReview().getId()))
        .thenReturn(expected);

    String requestBody = mapper.writeValueAsString(reply);

    MockHttpServletRequestBuilder mockRequest =
        post(REPLIES_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestBody);

    // Act & Assert
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.id", is(expected.getId())))
        .andExpect(jsonPath("$.reply_comment", is(expected.getReplyComment())))
        .andExpect(jsonPath("$.created_at", is(expected.getCreatedAt())))
        .andExpect(jsonPath("$.reply_user.username", is(expected.getReplyUser().getUsername())))
        .andExpect(jsonPath("$.review.comment", is(expected.getReview().getComment())));

    verify(replyServiceMock, times(1)).createNew(any(), any(), any());
  }

  @Test
  void addNewReply_noUser_throwsException() throws Exception {
    // Arrange
    ReplyEntity expected = REPLY_1;
    when(replyServiceMock.createNew(
            expected.getReplyComment(),
            expected.getReplyUser().getId(),
            expected.getReview().getId()))
        .thenThrow(NoSuchElementException.class);
    when(securityServiceMock.isSelfPostReply(any(), any())).thenReturn(true);

    String requestBody =
        mapper.writeValueAsString(
            ReplyPostDto.builder()
                .replyComment(expected.getReplyComment())
                .replyUserId(expected.getReplyUser().getId())
                .reviewId(expected.getReview().getId())
                .build());

    MockHttpServletRequestBuilder mockRequest =
        post(REPLIES_ENDPOINT)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .content(requestBody);

    // Act & Assert
    mockMvc.perform(mockRequest).andExpect(status().isNotFound());

    verify(replyServiceMock, times(1)).createNew(any(), any(), any());
  }

  @Test
  void getReply_returnsSuccessfully() throws Exception {
    // Arrange
    ReplyEntity expected = REPLY_1;
    when(replyServiceMock.getOne(REPLY_ID_1)).thenReturn(expected);

    MockHttpServletRequestBuilder mockRequest =
        get(REPLIES_ENDPOINT + REPLY_ID_1).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$.id", is(expected.getId())))
        .andExpect(jsonPath("$.reply_comment", is(expected.getReplyComment())))
        .andExpect(jsonPath("$.created_at", is(expected.getCreatedAt())))
        .andExpect(jsonPath("$.reply_user.username", is(expected.getReplyUser().getUsername())))
        .andExpect(jsonPath("$.review.comment", is(expected.getReview().getComment())));

    verify(replyServiceMock, times(1)).getOne(any());
  }

  @Test
  void getReply_noReply_throwsException() throws Exception {
    // Arrange
    when(replyServiceMock.getOne(REPLY_ID_1)).thenThrow(NoSuchElementException.class);

    MockHttpServletRequestBuilder mockRequest =
        get(REPLIES_ENDPOINT + REPLY_ID_1).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc.perform(mockRequest).andExpect(status().isNotFound());

    verify(replyServiceMock, times(1)).getOne(any());
  }

  @Test
  void getRepliesByUserId_returnsList() throws Exception {
    // Arrange
    when(replyServiceMock.getRepliesByUser(USER_ID_1)).thenReturn(List.of(REPLY_1, REPLY_2));

    MockHttpServletRequestBuilder mockRequest =
        get(REPLIES_ENDPOINT + USER_ROUTE + USER_ID_1).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)));

    verify(replyServiceMock, times(1)).getRepliesByUser(any());
  }

  @Test
  void getRepliesByUserId_returnsEmpty() throws Exception {
    // Arrange
    when(replyServiceMock.getRepliesByUser(USER_ID_1)).thenReturn(List.of());

    MockHttpServletRequestBuilder mockRequest =
        get(REPLIES_ENDPOINT + USER_ROUTE + USER_ID_1).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(0)));

    verify(replyServiceMock, times(1)).getRepliesByUser(any());
  }

  @Test
  void getRepliesByReviewId_returnsList() throws Exception {
    // Arrange
    when(replyServiceMock.getRepliesByReview(REVIEW_ID_1)).thenReturn(List.of(REPLY_2, REPLY_3));

    MockHttpServletRequestBuilder mockRequest =
        get(REPLIES_ENDPOINT + REVIEW_ROUTE + REVIEW_ID_1).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(2)));

    verify(replyServiceMock, times(1)).getRepliesByReview(any());
  }

  @Test
  void getRepliesByReviewId_returnsEmpty() throws Exception {
    // Arrange
    when(replyServiceMock.getRepliesByReview(REVIEW_ID_1)).thenReturn(List.of());

    MockHttpServletRequestBuilder mockRequest =
        get(REPLIES_ENDPOINT + REVIEW_ROUTE + REVIEW_ID_1).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc
        .perform(mockRequest)
        .andExpect(status().isOk())
        .andExpect(jsonPath("*", notNullValue()))
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$", hasSize(0)));

    verify(replyServiceMock, times(1)).getRepliesByReview(any());
  }

  @Test
  void deleteReply_deletesSuccessfully() throws Exception {
    // Arrange

    MockHttpServletRequestBuilder mockRequest =
        delete(REPLIES_ENDPOINT + REPLY_ID_1).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc.perform(mockRequest).andExpect(status().isNoContent());

    verify(replyServiceMock, times(1)).deleteOne(any());
  }

  @Test
  void deleteReply_noReply_throwsException() throws Exception {
    // Arrange
    doThrow(NoSuchElementException.class).when(replyServiceMock).deleteOne(REPLY_ID_2);

    MockHttpServletRequestBuilder mockRequest =
        delete(REPLIES_ENDPOINT + REPLY_ID_2).contentType(MediaType.APPLICATION_JSON);

    // Act & Assert
    mockMvc.perform(mockRequest).andExpect(status().isNotFound());

    verify(replyServiceMock, times(1)).deleteOne(any());
  }
}
