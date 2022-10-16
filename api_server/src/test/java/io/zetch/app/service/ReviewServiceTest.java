package io.zetch.app.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import java.util.List;
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
  @Mock private LocationRepository restaurantRepositoryMock;
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
    assertThat(reviewService.getAll().size(), is(3));
    assertThat(reviewService.getAll().get(0), is(reviewMock));
  }

  // VERIFY INVOCATION OF DEPS + PARAMETERS

  @Test
  public void createNew() {
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
    when(userRepositoryMock.findById(anyLong())).thenReturn(Optional.of(testUser));
    when(restaurantRepositoryMock.findById(anyLong())).thenReturn(Optional.of(testLocation));
    when(reviewRepositoryMock.save(any(ReviewEntity.class))).thenReturn(testReview);

    ReviewEntity r = reviewService.createNew("test review", 3, 1L, 1L);
    assertThat(testReview.toString(), is(r.toString()));
  }
}
