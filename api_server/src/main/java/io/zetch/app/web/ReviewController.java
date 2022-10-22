package io.zetch.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.review.ReviewPostDto;
import io.zetch.app.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

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
  @ResponseBody
  ReviewGetDto addNewUser(@RequestBody ReviewPostDto newReviewDto) {
    ReviewEntity r =
        reviewService.createNew(
            newReviewDto.getComment(),
            newReviewDto.getRating(),
            newReviewDto.getUserId(),
            newReviewDto.getLocationId());
    try {
      String serialized = mapper.writeValueAsString(r);
      return mapper.readValue(serialized, ReviewGetDto.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(JSON_PARSE_ERROR_MSG);
    }
  }

  /**
   * @return A list of all restraurants
   */
  @GetMapping(path = "/")
  @Operation(summary = "Retrieve all reviews")
  @ResponseBody
  Iterable<ReviewGetDto> getAllReviews() {
    try {
      var result = new ArrayList<ReviewGetDto>();
      for (var x : reviewService.getAll().stream().toList()) {
        result.add(mapper.readValue(mapper.writeValueAsString(x), ReviewGetDto.class));
      }
      return result;
    } catch (JsonProcessingException e) {
      throw new RuntimeException(JSON_PARSE_ERROR_MSG);
    }
  }

  /**
   * @param reviewId Review's id
   * @return A review with id
   */
  @GetMapping("/{reviewId}")
  @Operation(summary = "Retrieve a single restaurant")
  ReviewGetDto getOneReview(@PathVariable Long reviewId) {
    ReviewEntity review = reviewService.getOne(reviewId);
    try {
      String serialized = mapper.writeValueAsString(review);
      return mapper.readValue(serialized, ReviewGetDto.class);
    } catch (JsonProcessingException e) {
      throw new RuntimeException(JSON_PARSE_ERROR_MSG);
    }
  }
}
