package io.zetch.app.service;

import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.repo.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.when;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceTest {

  @Mock private ReviewRepository reviewRepositoryMock;
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
    ArgumentCaptor<ReviewEntity> reviewCaptor = ArgumentCaptor.forClass(ReviewEntity.class);

    reviewService.createNew("test review");

    // Verify save() invoked
    verify(reviewRepositoryMock).save(reviewCaptor.capture());

    // Verify the attributes of the Restaurant object
    ReviewEntity value = reviewCaptor.getValue();
    assertThat(value.getComment(), is("test review"));
  }
}
