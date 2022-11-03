package io.zetch.app.domain.reply;

import java.io.Serializable;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ReplyDto implements Serializable {
  @ToString.Exclude Long id;
  String replyComment;
  Long createdAt;
}
