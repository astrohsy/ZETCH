package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.review.ReviewPostDto;
import io.zetch.app.service.ReviewService;
import java.util.Optional;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

/** Controller for the review endpoints. */
@RestController
@RequestMapping(path = "/reviews")
@Tag(name = "Reviews")
@CrossOrigin(origins = "*") // NOSONAR
public class ReviewController {
  private final ReviewService reviewService;

  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping()
  @Operation(summary = "Create a new review.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  ReviewGetDto addNewReview(@Valid @RequestBody ReviewPostDto newReviewDto) {
    ReviewEntity r =
        reviewService.createNew(
            newReviewDto.comment(),
            newReviewDto.rating(),
            newReviewDto.userId(),
            newReviewDto.locationId());
    return r.toGetDto();
  }

  /** Returns a list of all reviews. */
  @GetMapping()
  @Operation(summary = "Retrieve all reviews.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<ReviewGetDto> getAllReviews(
      @RequestParam Optional<Long> locationId, @RequestParam Optional<Long> userId) {
    return reviewService.getAll(locationId, userId).stream().map(ReviewEntity::toGetDto).toList();
  }

  /**
   * Returns a review with id.
   *
   * @param reviewId Review's id
   * @return A review with id
   */
  @GetMapping("/{reviewId}")
  @Operation(summary = "Retrieve a review with reviewId.")
  @SecurityRequirement(name = "OAuth2")
  ReviewGetDto getOneReview(@PathVariable Long reviewId) {
    ReviewEntity review = reviewService.getOne(reviewId);
    return review.toGetDto();
  }

  /**
   * Deletes a review.
   *
   * @param reviewId Review's id
   * @return Nothing if successful
   */
  @DeleteMapping("/{reviewId}")
  @Operation(summary = "Delete a review with reviewId.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  @PreAuthorize("@securityService.isOwnedReview(#token, #reviewId)")
  void deleteOneReview(@PathVariable Long reviewId, JwtAuthenticationToken token) {
    reviewService.deleteOne(reviewId);
  }
}
