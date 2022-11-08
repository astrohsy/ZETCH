package io.zetch.app.domain.reply;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.user.UserGetDto;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

/** GET DTO for the Reply entity. */
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyGetDto implements Serializable {
  @ToString.Exclude Long id;

  @JsonProperty("reply_comment")
  String replyComment;

  @JsonProperty("created_at")
  Long createdAt;

  @JsonProperty("reply_user")
  UserGetDto replyUser;

  ReviewGetDto review;
}
