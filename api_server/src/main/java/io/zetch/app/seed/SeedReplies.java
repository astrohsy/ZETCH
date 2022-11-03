package io.zetch.app.seed;

import io.zetch.app.domain.reply.ReplyEntity;
import java.util.Date;
import java.util.List;

public class SeedReplies {
  public static ReplyEntity REPLY_1 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER_5)
          .replyComment("Thanks for visiting our location!")
          .createdAt(new Date("09/05/2022").getTime())
          .review(SeedReviews.REVIEW_1)
          .build();

  public static ReplyEntity REPLY_2 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER_4)
          .replyComment("Glad you enjoyed your meal!")
          .createdAt(new Date("08/05/2021").getTime())
          .review(SeedReviews.REVIEW_2)
          .build();

  public static ReplyEntity REPLY_3 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER_4)
          .replyComment("Please let us know what we can do to improve your next experience.")
          .createdAt(new Date("07/13/2021").getTime())
          .review(SeedReviews.REVIEW_3)
          .build();

  public static ReplyEntity REPLY_4 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER_3)
          .replyComment("We loved having you at our museum.")
          .createdAt(new Date("11/22/2020").getTime())
          .review(SeedReviews.REVIEW_4)
          .build();

  public static ReplyEntity REPLY_5 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER_1)
          .replyComment("Hope you had a great time!")
          .createdAt(new Date("01/02/2021").getTime())
          .review(SeedReviews.REVIEW_5)
          .build();

  public static final List<ReplyEntity> REPLIES =
      List.of(REPLY_1, REPLY_2, REPLY_3, REPLY_4, REPLY_5);
}
