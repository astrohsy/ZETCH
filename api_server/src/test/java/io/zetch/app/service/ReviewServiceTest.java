package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
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
public class ReviewServiceTest {

  @Mock private UserRepository userRepositoryMock;
  @Mock private ReviewRepository reviewRepositoryMock;
  @Mock private LocationRepository locationRepositoryMock;
  @InjectMocks private ReviewService reviewService;
  @Mock private ReviewEntity reviewMock;

  // VERIFY SERVICE RETURN VALUE

  @Test
  public void getOne() {
    when(reviewRepositoryMock.findById(1L)).thenReturn(Optional.of(reviewMock));
    assertThat(reviewService.getOne(1L), is(reviewMock));
  }

  @Test
  public void getAll() {
    when(reviewRepositoryMock.findAll()).thenReturn(List.of(reviewMock, reviewMock, reviewMock));
    assertThat(reviewService.getAll(Optional.empty(), Optional.empty()).size(), is(3));
  }

  @Test
  public void getAll_withLocationId() {
    reviewService.getAll(Optional.of(1L), Optional.empty());
    verify(reviewRepositoryMock, times(1)).findByLocationId(anyLong());
  }

  @Test
  public void getAll_withUserId() {
    reviewService.getAll(Optional.empty(), Optional.of(1L));
    verify(reviewRepositoryMock, times(1)).findByUserId(anyLong());
  }

  @Test
  public void getAll_withUserIdAnLocationId() {
    reviewService.getAll(Optional.of(1L), Optional.of(1L));
    verify(reviewRepositoryMock, times(1)).findByUserIdAndLocationId(anyLong(), anyLong());
  }


  // VERIFY INVOCATION OF DEPS + PARAMETERS

  @Test
  public void createNew() {
    Long validUserId = 1L;
    Long validLocationId = 1L;

    UserEntity testUser = UserEntity.builder().username("test").email("helo@hello.com").build();
    LocationEntity testLocation =
        LocationEntity.builder().name("test").address("155 Claremont NY").build();
    ReviewEntity testReview =
        ReviewEntity.builder()
            .comment("test")
            .rating(3)
            .user(testUser)
            .location(testLocation)
            .build();
    when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
    when(userRepositoryMock.findById(eq(validUserId))).thenReturn(Optional.of(testUser));
    when(locationRepositoryMock.findById(anyLong())).thenReturn(Optional.empty());
    when(locationRepositoryMock.findById(eq(validLocationId)))
        .thenReturn(Optional.of(testLocation));
    when(reviewRepositoryMock.save(any(ReviewEntity.class))).thenReturn(testReview);

    ReviewEntity r = reviewService.createNew("test review", 3, validUserId, validLocationId);
    assertThat(testReview.toString(), is(r.toString()));

    Exception exceptionWhenNoUser =
        assertThrows(
            NoSuchElementException.class,
            () -> {
              reviewService.createNew("test review", 3, 123L, validLocationId);
            });

    Exception exceptionWhenNoLocation =
        assertThrows(
            NoSuchElementException.class,
            () -> {
              reviewService.createNew("test review", 3, validUserId, 123L);
            });
    String expectedMessage = "User or Location is not exist";

    assertTrue(exceptionWhenNoUser.getMessage().equals(expectedMessage));
    assertTrue(exceptionWhenNoLocation.getMessage().equals(expectedMessage));
  }

  @Test
  public void deleteOne() {
    doReturn(true).when(reviewRepositoryMock).existsById(1L);
    reviewService.deleteOne(1L);

    doReturn(false).when(reviewRepositoryMock).existsById(2L);
    assertThrows(
        NoSuchElementException.class,
        () -> {
          reviewService.deleteOne(2L);
        });
  }
}
