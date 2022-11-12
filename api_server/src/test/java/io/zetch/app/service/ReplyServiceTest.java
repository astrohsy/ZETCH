package io.zetch.app.service;

import static io.zetch.app.TestConstants.CREATED_BY;
import static io.zetch.app.TestConstants.REPLY_ID_1;
import static io.zetch.app.TestConstants.REVIEW_ID_1;
import static io.zetch.app.TestConstants.USERNAME_1;
import static io.zetch.app.TestConstants.USER_ID_1;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.reply.ReplyEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.ReplyRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ReplyServiceTest {

  @Mock private ReplyRepository replyRepositoryMock;
  @Mock private UserRepository userRepositoryMock;
  @Mock private ReviewRepository reviewRepositoryMock;
  @InjectMocks private ReplyService replyService;
  @Mock private ReplyEntity replyMock;
  @Mock private UserEntity userMock;
  @Mock private ReviewEntity reviewMock;
  @Mock private LocationEntity locationMock;

  @Test
  void getOne_ReturnsSuccessfully() {
    // Arrange
    when(replyRepositoryMock.findById(1L)).thenReturn(Optional.of(replyMock));

    // Act
    ReplyEntity output = replyService.getOne(1L);

    // Assert
    assertThat(output, is(replyMock));
  }

  @Test
  void getOne_ThrowsElementException() {
    // Arrange
    String expectedMessage = "Reply does not exist: " + REPLY_ID_1;
    when(replyRepositoryMock.findById(REPLY_ID_1)).thenReturn(Optional.empty());

    // Act
    Exception exception =
        assertThrows(NoSuchElementException.class, () -> replyService.getOne(REPLY_ID_1));

    // Assert
    assertEquals(exception.getMessage(), expectedMessage);
  }

  @Test
  void getRepliesByUser_ReturnsList() {
    // Arrange
    when(replyRepositoryMock.findByReplyUserId(USER_ID_1))
        .thenReturn(List.of(replyMock, replyMock, replyMock));

    // Act
    List<ReplyEntity> output = replyService.getRepliesByUser(USER_ID_1);

    // Assert
    assertThat(output.size(), is(3));
    assertThat(output.get(0), is(replyMock));
  }

  @Test
  void getRepliesByUser_ReturnsEmpty() {
    // Arrange
    when(replyRepositoryMock.findByReplyUserId(USER_ID_1)).thenReturn(List.of());

    // Act
    List<ReplyEntity> output = replyService.getRepliesByUser(USER_ID_1);

    // Assert
    assertThat(output.size(), is(0));
  }

  @Test
  void getRepliesByReview_returnsList() {
    // Arrange
    when(replyRepositoryMock.findByReviewId(REVIEW_ID_1))
        .thenReturn(List.of(replyMock, replyMock, replyMock));

    // Act
    List<ReplyEntity> output = replyService.getRepliesByReview(REVIEW_ID_1);

    // Assert
    assertThat(output.size(), is(3));
    assertThat(output.get(0), is(replyMock));
  }

  @Test
  void getRepliesByReview_ReturnsEmpty() {
    // Arrange
    when(replyRepositoryMock.findByReviewId(REVIEW_ID_1)).thenReturn(List.of());

    // Act
    List<ReplyEntity> output = replyService.getRepliesByReview(REVIEW_ID_1);

    // Assert
    assertThat(output.size(), is(0));
  }

  @Test
  void createNew_createsSuccessfully() {
    // Arrange
    ReplyEntity expected =
        ReplyEntity.builder()
            .replyComment("This is a reply")
            .replyUser(userMock)
            .review(reviewMock)
            .createdAt(CREATED_BY)
            .build();

    when(userRepositoryMock.findById(USER_ID_1)).thenReturn(Optional.of(userMock));
    when(reviewRepositoryMock.findById(REVIEW_ID_1)).thenReturn(Optional.of(reviewMock));
    when(replyRepositoryMock.save(any(ReplyEntity.class))).thenReturn(expected);
    when(reviewMock.getLocation()).thenReturn(locationMock);
    when(locationMock.getOwners()).thenReturn(List.of(userMock));
    when(userMock.getUsername()).thenReturn(USERNAME_1);

    // Act
    ReplyEntity output = replyService.createNew("This is a reply", USER_ID_1, REVIEW_ID_1);

    // Assert
    assertEquals(output, expected);
  }

  @Test
  void createNew_noUser_ThrowsException() {
    // Arrange
    String expectedMessage = "User does not exist: " + USER_ID_1;
    when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
    when(reviewRepositoryMock.findById(REVIEW_ID_1)).thenReturn(Optional.of(reviewMock));

    // Act
    Exception exception =
        assertThrows(
            NoSuchElementException.class,
            () -> replyService.createNew("This is a reply", USER_ID_1, REVIEW_ID_1));

    // Assert
    assertEquals(exception.getMessage(), expectedMessage);
  }

  @Test
  void createNew_noReview_ThrowsException() {
    // Arrange
    String expectedMessage = "Review does not exist: " + REVIEW_ID_1;
    when(reviewRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
    when(userRepositoryMock.findById(USER_ID_1)).thenReturn(Optional.of(userMock));

    // Act
    Exception exception =
        assertThrows(
            NoSuchElementException.class,
            () -> replyService.createNew("This is a reply", USER_ID_1, REVIEW_ID_1));

    // Assert
    assertEquals(exception.getMessage(), expectedMessage);
  }

  @Test
  public void createNew_notOwner_ThrowsException() {
    // Arrange
    String expectedMessage = "User is not the owner of the reviewed location.";
    when(userRepositoryMock.findById(USER_ID_1)).thenReturn(Optional.of(userMock));
    when(reviewRepositoryMock.findById(REVIEW_ID_1)).thenReturn(Optional.of(reviewMock));
    when(reviewMock.getLocation()).thenReturn(locationMock);
    when(locationMock.getOwners()).thenReturn(List.of());
    when(userMock.getUsername()).thenReturn(USERNAME_1);

    // Act
    Exception exception =
        assertThrows(
            IllegalArgumentException.class,
            () -> replyService.createNew("This is a reply", USER_ID_1, REVIEW_ID_1));

    // Assert
    assertEquals(exception.getMessage(), expectedMessage);
  }

  @Test
  public void deleteOne_deletesSuccessfully() {
    // Arrange
    when(replyRepositoryMock.existsById(REPLY_ID_1)).thenReturn(true);

    // Act
    replyService.deleteOne(REPLY_ID_1);

    // Assert
    verify(replyRepositoryMock, times(1)).deleteById(REPLY_ID_1);
  }

  @Test
  void deleteOne_throwsException() {
    // Arrange
    when(replyRepositoryMock.existsById(REPLY_ID_1)).thenReturn(false);
    String expectedMessage = "Reply does not exist: " + REPLY_ID_1;

    // Act
    Exception exception =
        assertThrows(NoSuchElementException.class, () -> replyService.deleteOne(REPLY_ID_1));

    // Assert
    assertEquals(exception.getMessage(), expectedMessage);
    verify(replyRepositoryMock, times(0)).deleteById(REPLY_ID_1);
  }
}
