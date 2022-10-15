package io.zetch.app.service;

import io.zetch.app.domain.restaurant.RestaurantEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.RestaurantRepository;
import io.zetch.app.repo.ReviewRepository;
import java.util.List;
import java.util.NoSuchElementException;

import io.zetch.app.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Review business logic */
@Service
public class ReviewService {
  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final RestaurantRepository restaurantRepository;

  @Autowired
  public ReviewService(
      ReviewRepository reviewRepository,
      UserRepository userRepository,
      RestaurantRepository restaurantRepository) {
    this.reviewRepository = reviewRepository;
    this.userRepository = userRepository;
    this.restaurantRepository = restaurantRepository;
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
  public ReviewEntity createNew(
      String comment, Integer rating, String username, Long restaurantId) {
    UserEntity u = userRepository.findById(username).orElse(null);
    RestaurantEntity r = restaurantRepository.findById(restaurantId).orElse(null);

    if (u == null || r == null) {
      throw new NoSuchElementException("User or Restaurant is not exist");
    }

    ReviewEntity newReview =
        ReviewEntity.builder().comment(comment).rating(rating).user(u).restaurant(r).build();
    return reviewRepository.save(newReview);
  }
}
