package io.zetch.app.domain.reply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyPostDto {
  @JsonProperty("reply_comment")
  String replyComment;

  @JsonProperty("user_id")
  Long replyUserId;

  @JsonProperty("review_id")
  Long reviewId;
}
