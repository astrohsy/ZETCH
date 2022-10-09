package io.zetch.app.service;

import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.repo.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

/** Review business logic */
@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;

  @Autowired
  public ReviewService(ReviewRepository reviewRepository) {
    this.reviewRepository = reviewRepository;
  }

  /**
   * Retrieve all Reviews
   *
   * @return List of all Reviews
   */
  public List<ReviewEntity> getAll() {
    return reviewRepository.findAll();
  }

  /**
   * Retrieve one Review
   *
   * @param id Review id
   * @return Review
   * @throws NoSuchElementException If Review not found
   */
  public ReviewEntity getOne(Long id) throws NoSuchElementException {
    return reviewRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Review does not exist: " + id));
  }

  /**
   * Create a new Review in the database
   *
   * @param comment Review comment
   */
  public ReviewEntity createNew(String comment) {
    ReviewEntity newReview = ReviewEntity.builder().comment(comment).build();
    return reviewRepository.save(newReview);
  }
}
