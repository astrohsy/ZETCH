package io.zetch.app.domain.reply;

import io.zetch.app.domain.review.ReviewGetDto;
import io.zetch.app.domain.user.UserGetDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReplyGetDto extends ReplyDto {
  UserGetDto replyUser;
  ReviewGetDto Review;
}
