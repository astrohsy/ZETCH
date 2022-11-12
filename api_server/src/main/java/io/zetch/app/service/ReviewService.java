package io.zetch.app.service;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.LocationRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Review business logic. */
@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final LocationRepository locationRepository;

  /** Review service constructor. */
  @Autowired
  public ReviewService(
      ReviewRepository reviewRepository,
      UserRepository userRepository,
      LocationRepository locationRepository) {
    this.reviewRepository = reviewRepository;
    this.userRepository = userRepository;
    this.locationRepository = locationRepository;
  }

  /**
   * Retrieve all Reviews.
   *
   * @return List of all Reviews
   */
  public List<ReviewEntity> getAll(Optional<Long> locationId, Optional<Long> userId) {
    if (locationId.isPresent() && userId.isPresent())
      return reviewRepository.findByUserIdAndLocationId(userId.get(), locationId.get());
    else if (locationId.isPresent()) return reviewRepository.findByLocationId(locationId.get());
    else if (userId.isPresent()) return reviewRepository.findByUserId(userId.get());
    return reviewRepository.findAll();
  }

  /**
   * Retrieve one Review.
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
   * Create a new Review in the database.
   *
   * @param comment Review comment
   */
  public ReviewEntity createNew(String comment, Integer rating, Long userId, Long locationId) {
    UserEntity u = userRepository.findById(userId).orElse(null);
    LocationEntity l = locationRepository.findById(locationId).orElse(null);

    if (u == null || l == null) {
      throw new NoSuchElementException("User or Location is not exist");
    }

    ReviewEntity newReview =
        ReviewEntity.builder().comment(comment).rating(rating).user(u).location(l).build();
    return reviewRepository.save(newReview);
  }

  /** Deletes one review. */
  public void deleteOne(Long reviewId) {
    if (!reviewRepository.existsById(reviewId)) {
      throw new NoSuchElementException();
    }
    reviewRepository.deleteById(reviewId);
  }
}
