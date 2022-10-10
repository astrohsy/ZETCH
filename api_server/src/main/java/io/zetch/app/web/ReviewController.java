package io.zetch.app.web;

import com.google.gson.Gson;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.review.ReviewDto;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.service.ReviewService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/reviews")
@Tag(name = "Reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
  ModelMapper modelMapper = new ModelMapper();
  private final ReviewService reviewService;
  private Gson g = new Gson();

  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  /**
   * @return A list of all restraurants
   */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all reviews")
  @ResponseBody
  Iterable<ReviewDto> getAllReviews() {
    return reviewService.getAll().stream()
        .map(r -> g.fromJson(g.toJson(r), ReviewDto.class))
        .toList();
  }

  /**
   * @param reviewId Review's id
   * @return A review with id
   */
  @GetMapping("/{reviewId}")
  @Operation(summary = "Retrieve a single restaurant")
  ReviewDto getOneReview(@PathVariable Long reviewId) {
    ReviewEntity review = reviewService.getOne(reviewId);
    return modelMapper.map(review, ReviewDto.class);
  }
}
