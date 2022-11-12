package io.zetch.app.seed;

import io.zetch.app.domain.reply.ReplyEntity;
import java.util.Date;
import java.util.List;

/** Helper class for adding sample replies. */
public class SeedReplies {

  private SeedReplies() {
    throw new IllegalStateException("Utility class");
  }

  public static final ReplyEntity REPLY1 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER5)
          .replyComment("Thanks for visiting our location!")
          .createdAt(new Date("09/05/2022").getTime())
          .review(SeedReviews.REVIEW1)
          .build();

  public static final ReplyEntity REPLY2 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER4)
          .replyComment("Glad you enjoyed your meal!")
          .createdAt(new Date("08/05/2021").getTime())
          .review(SeedReviews.REVIEW2)
          .build();

  public static final ReplyEntity REPLY3 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER4)
          .replyComment("Please let us know what we can do to improve your next experience.")
          .createdAt(new Date("07/13/2021").getTime())
          .review(SeedReviews.REVIEW3)
          .build();

  public static final ReplyEntity REPLY4 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER3)
          .replyComment("We loved having you at our museum.")
          .createdAt(new Date("11/22/2020").getTime())
          .review(SeedReviews.REVIEW4)
          .build();

  public static final ReplyEntity REPLY5 =
      ReplyEntity.builder()
          .replyUser(SeedUsers.USER1)
          .replyComment("Hope you had a great time!")
          .createdAt(new Date("01/02/2021").getTime())
          .review(SeedReviews.REVIEW5)
          .build();

  public static final List<ReplyEntity> REPLIES = List.of(REPLY1, REPLY2, REPLY3, REPLY4, REPLY5);
}
