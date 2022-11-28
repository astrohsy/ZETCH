package io.zetch.app.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.zetch.app.domain.reply.ReplyEntity;
import io.zetch.app.domain.reply.ReplyGetDto;
import io.zetch.app.domain.reply.ReplyPostDto;
import io.zetch.app.service.ReplyService;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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

/** Controller for the Reply endpoints. */
@RestController
@RequestMapping(path = "/replies")
@Tag(name = "Replies")
@CrossOrigin(origins = "*") // NOSONAR
public class ReplyController {
  private final ReplyService replyService;

  @Autowired
  public ReplyController(ReplyService replyService) {
    this.replyService = replyService;
  }

  @PostMapping(path = "/")
  @Operation(summary = "Create a new reply. Auth: user must be the owner of the reviewed location.")
  @SecurityRequirement(name = "OAuth2")
  @PreAuthorize("@securityService.isSelfPostReply(#token, #newReply)")
  @ResponseBody
  ReplyGetDto addNewReply(@RequestBody ReplyPostDto newReply, JwtAuthenticationToken token) {
    ReplyEntity reply =
        replyService.createNew(
            newReply.getReplyComment(), newReply.getReplyUserId(), newReply.getReviewId());

    return reply.toGetDto();
  }

  @GetMapping("/{replyId}")
  @Operation(summary = "Retrieve a reply with replyId")
  @SecurityRequirement(name = "OAuth2")
  ReplyGetDto getReply(@PathVariable Long replyId, JwtAuthenticationToken token) {
    return replyService.getOne(replyId).toGetDto();
  }

  @GetMapping("/user/{userId}")
  @Operation(summary = "Retrieve a reply with UserId")
  @SecurityRequirement(name = "OAuth2")
  List<ReplyGetDto> getRepliesByUserId(@PathVariable Long userId, JwtAuthenticationToken token) {
    return replyService.getRepliesByUser(userId).stream().map(ReplyEntity::toGetDto).toList();
  }

  @GetMapping("/review/{reviewId}")
  @Operation(summary = "Retrieve a reply with ReviewId")
  @SecurityRequirement(name = "OAuth2")
  List<ReplyGetDto> getRepliesByReviewId(
      @PathVariable Long reviewId, JwtAuthenticationToken token) {
    return replyService.getRepliesByReview(reviewId).stream().map(ReplyEntity::toGetDto).toList();
  }

  /** Deletes a reply with replyId. */
  @DeleteMapping("/{replyId}")
  @Operation(summary = "Delete a reply with replyId")
  @SecurityRequirement(name = "OAuth2")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteOneReply(@PathVariable Long replyId, JwtAuthenticationToken token) {
    replyService.deleteOne(replyId);
  }

  /**
   * Exception handler if NoSuchElementException is thrown in this Controller.
   *
   * @param ex Exception
   * @return Error message string
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler(NoSuchElementException.class)
  String return404(NoSuchElementException ex) {
    return ex.getMessage();
  }

  /**
   * Return 400 Bad Request if IllegalArgumentException is thrown in this Controller.
   *
   * @param ex Exception
   * @return Error message string
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(IllegalArgumentException.class)
  String return404(IllegalArgumentException ex) {
    return ex.getMessage();
  }
}
