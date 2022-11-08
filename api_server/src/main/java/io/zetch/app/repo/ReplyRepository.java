package io.zetch.app.repo;

import io.zetch.app.domain.reply.ReplyEntity;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/** Backend for Replies. */
public interface ReplyRepository extends JpaRepository<ReplyEntity, Long> {
  List<ReplyEntity> findByReplyUserId(Long userId);

  List<ReplyEntity> findByReviewId(Long reviewId);
}
