package io.zetch.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.reply.ReplyEntity;
import io.zetch.app.domain.reply.ReplyGetDto;
import io.zetch.app.domain.reply.ReplyPostDto;
import io.zetch.app.service.ReplyService;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/replies")
@Tag(name = "Replies")
@CrossOrigin(origins = "*") // NOSONAR
public class ReplyController {
  private final ReplyService replyService;
  private final ObjectMapper mapper =
      new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

  @Autowired
  public ReplyController(ReplyService replyService) {
    this.replyService = replyService;
  }

  @PostMapping(path = "/")
  @Operation(summary = "Create a new reply")
  @SecurityRequirement(name = "OAuth2")
  @ResponseBody
  ReplyGetDto addNewReply(@RequestBody ReplyPostDto newReply) throws JsonProcessingException {
    ReplyEntity reply =
        replyService.createNew(
            newReply.getReplyComment(), newReply.getReplyUserId(), newReply.getReviewId());
    String serialized = mapper.writeValueAsString(reply);
    return mapper.readValue(serialized, ReplyGetDto.class);
  }

  @GetMapping("/{replyId}")
  @Operation(summary = "Retrieve a reply with replyId")
  @SecurityRequirement(name = "OAuth2")
  ReplyGetDto getReply(@PathVariable Long replyId) throws JsonProcessingException {
    ReplyEntity reply = replyService.getOne(replyId);
    String serialized = mapper.writeValueAsString(reply);
    return mapper.readValue(serialized, ReplyGetDto.class);
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Retrieve a reply with UserId")
  @SecurityRequirement(name = "OAuth2")
  Iterable<ReplyGetDto> getRepliesByUserId(@PathVariable Long userId)
      throws JsonProcessingException {
    List<ReplyEntity> replies = replyService.getRepliesByUser(userId).stream().toList();
    var result = new ArrayList<ReplyGetDto>();
    for (var reply : replies) {
      result.add(mapper.readValue(mapper.writeValueAsString(reply), ReplyGetDto.class));
    }
    return result;
  }

  @GetMapping("/review/{reviewId}")
  @Operation(summary = "Retrieve a reply with ReviewId")
  @SecurityRequirement(name = "OAuth2")
  Iterable<ReplyGetDto> getRepliesByReviewId(@PathVariable Long reviewId)
      throws JsonProcessingException {
    List<ReplyEntity> replies = replyService.getRepliesByReview(reviewId).stream().toList();
    var result = new ArrayList<ReplyGetDto>();
    for (var reply : replies) {
      result.add(mapper.readValue(mapper.writeValueAsString(reply), ReplyGetDto.class));
    }
    return result;
  }

  @DeleteMapping("/{replyId}")
  @Operation(summary = "Delete a reply with replyId")
  @SecurityRequirement(name = "OAuth2")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteOneReply(@PathVariable Long replyId) {
    replyService.deleteOne(replyId);
  }

  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  String return404(NoSuchElementException ex) {
    return ex.getMessage();
  }
}
