package io.zetch.app.domain.reply;

import io.zetch.app.domain.BaseEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.UserEntity;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/** Reply entity definition. */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table
public class ReplyEntity extends BaseEntity {
  @ManyToOne
  @JoinColumn(name = "user_id")
  UserEntity replyUser;

  String replyComment;

  @ManyToOne
  @JoinColumn(name = "review_id")
  ReviewEntity review;

  Long createdAt;

  public ReplyGetDto toGetDto() {
    return new ReplyGetDto(
        super.getId(), replyComment, createdAt, replyUser.toGetDto(), review.toGetDto());
  }
}
