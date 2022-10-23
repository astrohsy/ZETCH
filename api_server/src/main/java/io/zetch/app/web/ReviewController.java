package io.zetch.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.review.ReviewPostDto;
import io.zetch.app.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.NoSuchElementException;

@RestController
@RequestMapping(path = "/reviews")
@Tag(name = "Reviews")
@CrossOrigin(origins = "*") // NOSONAR
public class ReviewController {
  private final ReviewService reviewService;
  private static final String JSON_PARSE_ERROR_MSG = "Cannot handle this json";
  private final ObjectMapper mapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Autowired
  public ReviewController(ReviewService reviewService) {
    this.reviewService = reviewService;
  }

  @PostMapping(path = "/")
  @Operation(summary = "Create a new review")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  ReviewGetDto addNewUser(@RequestBody ReviewPostDto newReviewDto) throws JsonProcessingException {
    ReviewEntity r =
        reviewService.createNew(
            newReviewDto.getComment(),
            newReviewDto.getRating(),
            newReviewDto.getUserId(),
            newReviewDto.getLocationId());
    String serialized = mapper.writeValueAsString(r);
    return mapper.readValue(serialized, ReviewGetDto.class);
  }

  /**
   * @return A list of all restraurants
   */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all reviews")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  Iterable<ReviewGetDto> getAllReviews() throws JsonProcessingException {
    var result = new ArrayList<ReviewGetDto>();
    for (var x : reviewService.getAll().stream().toList()) {
      result.add(mapper.readValue(mapper.writeValueAsString(x), ReviewGetDto.class));
    }
    return result;
  }

  /**
   * @param reviewId Review's id
   * @return A review with id
   */
  @GetMapping("/{reviewId}")
  @Operation(summary = "Retrieve a review with reviewId")
  @SecurityRequirement(name = "OAuth2")
  ReviewGetDto getOneReview(@PathVariable Long reviewId) throws JsonProcessingException {
    ReviewEntity review = reviewService.getOne(reviewId);
    String serialized = mapper.writeValueAsString(review);
    return mapper.readValue(serialized, ReviewGetDto.class);
  }

  @DeleteMapping("/{reviewId}")
  @Operation(summary = "Retrieve a review with reviewId")
  @SecurityRequirement(name = "OAuth2")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteOneReview(@PathVariable Long reviewId) {
    reviewService.deleteOne(reviewId);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  String return404(NoSuchElementException ex) {
    return ex.getMessage();
  }
}
