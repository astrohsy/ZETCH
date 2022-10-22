package io.zetch.app.web;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.review.ReviewPostDto;
import io.zetch.app.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/reviews")
@Tag(name = "Reviews")
@CrossOrigin(origins = "*") // NOSONAR
public class ReviewController {
  private final ReviewService reviewService;
  private final Gson g = new Gson();

  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping(path = "/")
  @Operation(summary = "Create a new review")
  @ResponseBody
  ReviewGetDto addNewUser(@RequestBody ReviewPostDto newReviewDto) {
    ReviewEntity r =
        reviewService.createNew(
            newReviewDto.getComment(),
            newReviewDto.getRating(),
            newReviewDto.getUserId(),
            newReviewDto.getLocationId());

    return g.fromJson(g.toJson(r), ReviewGetDto.class);
  }

  /**
   * @return A list of all restraurants
   */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all reviews")
  @ResponseBody
  Iterable<ReviewGetDto> getAllReviews() {
    return reviewService.getAll().stream()
        .map(r -> g.fromJson(g.toJson(r), ReviewGetDto.class))
        .toList();
  }

  /**
   * @param reviewId Review's id
   * @return A review with id
   */
  @GetMapping("/{reviewId}")
  @Operation(summary = "Retrieve a single restaurant")
  ReviewGetDto getOneReview(@PathVariable Long reviewId) {
    ReviewEntity review = reviewService.getOne(reviewId);
    return g.fromJson(g.toJson(review), ReviewGetDto.class);
  }
}
