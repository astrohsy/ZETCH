package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.review.ReviewPostDto;
import io.zetch.app.service.ReviewService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

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

  @PostMapping(path = "/")
  @Operation(summary = "Create a new review.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  ReviewGetDto addNewUser(@Valid @RequestBody ReviewPostDto newReviewDto) {
    ReviewEntity r =
        reviewService.createNew(
            newReviewDto.comment(),
            newReviewDto.rating(),
            newReviewDto.userId(),
            newReviewDto.locationId());
    return r.toGetDto();
  }

  /** Returns a list of all restraurants. */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all reviews.")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<ReviewGetDto> getAllReviews() {
    var result = new ArrayList<ReviewGetDto>();
    for (var x : reviewService.getAll().stream().toList()) {
      result.add(x.toGetDto());
    }
    return result;
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
  void deleteOneReview(@PathVariable Long reviewId) {
    reviewService.deleteOne(reviewId);
  }
}
