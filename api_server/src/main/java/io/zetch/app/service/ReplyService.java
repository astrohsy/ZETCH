package io.zetch.app.service;

import io.zetch.app.domain.reply.ReplyEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import io.zetch.app.repo.ReplyRepository;
import io.zetch.app.repo.ReviewRepository;
import io.zetch.app.repo.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Reply business logic */
@Service
public class ReplyService {

  private final ReplyRepository replyRepository;
  private final UserRepository userRepository;
  private final ReviewRepository reviewRepository;

  @Autowired
  public ReplyService(
      ReplyRepository replyRepository,
      UserRepository userRepository,
      ReviewRepository reviewRepository) {
    this.replyRepository = replyRepository;
    this.userRepository = userRepository;
    this.reviewRepository = reviewRepository;
  }

  /**
   * Retrieve one reply
   *
   * @param id Reply
   * @return Reply
   * @throws NoSuchElementException If Reply not found
   */
  public ReplyEntity getOne(Long id) throws NoSuchElementException {
    return replyRepository
        .findById(id)
        .orElseThrow(() -> new NoSuchElementException("Reply does not exist: " + id));
  }

  public List<ReplyEntity> getRepliesByUser(Long userId) {
    return replyRepository.findByReplyUserId(userId);
  }

  public List<ReplyEntity> getRepliesByReview(Long reviewId) {
    return replyRepository.findByReviewId(reviewId);
  }

  /**
   * Create a new Reply in the database
   *
   * @param replyComment Review comment
   */
  public ReplyEntity createNew(String replyComment, Long userId, Long reviewId) {
    UserEntity user = userRepository.findById(userId).orElse(null);
    ReviewEntity review = reviewRepository.findById(reviewId).orElse(null);

    if (user == null) {
      throw new NoSuchElementException("User does not exist: " + userId);
    }

    if (review == null) {
      throw new NoSuchElementException("Review does not exist: " + reviewId);
    }

    ReplyEntity newReply =
        ReplyEntity.builder()
            .replyComment(replyComment)
            .replyUser(user)
            .review(review)
            .createdAt(new Date().getTime())
            .build();

    return replyRepository.save(newReply);
  }

  public void deleteOne(Long replyId) {
    if (!replyRepository.existsById(replyId)) {
      throw new NoSuchElementException("Reply does not exist: " + replyId);
    }
    replyRepository.deleteById(replyId);
  }
}
