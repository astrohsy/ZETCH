package io.zetch.app.domain.reply;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReplyPostDto extends ReplyDto {
  @JsonProperty("user_id")
  Long replyUserId;

  @JsonProperty("review_id")
  Long reviewId;
}
