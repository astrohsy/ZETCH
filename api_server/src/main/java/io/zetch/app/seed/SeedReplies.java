package io.zetch.app.seed;

import io.zetch.app.domain.reply.ReplyEntity;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

/** Helper class for adding sample replies. */
public class SeedReplies {

  private static final SimpleDateFormat formatter =
      new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);

  public static final ReplyEntity REPLY1;

  static {
    try {
      REPLY1 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER5)
              .replyComment("Thanks for visiting our location!")
              .createdAt(formatter.parse("09/05/2022").getTime())
              .review(SeedReviews.REVIEW1)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final ReplyEntity REPLY2;

  static {
    try {
      REPLY2 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER4)
              .replyComment("Glad you enjoyed your meal!")
              .createdAt(formatter.parse("08/05/2021").getTime())
              .review(SeedReviews.REVIEW2)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final ReplyEntity REPLY3;

  static {
    try {
      REPLY3 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER4)
              .replyComment("Please let us know what we can do to improve your next experience.")
              .createdAt(formatter.parse("07/13/2021").getTime())
              .review(SeedReviews.REVIEW3)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final ReplyEntity REPLY4;

  static {
    try {
      REPLY4 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER3)
              .replyComment("We loved having you at our museum.")
              .createdAt(formatter.parse("11/22/2020").getTime())
              .review(SeedReviews.REVIEW4)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final ReplyEntity REPLY5;

  static {
    try {
      REPLY5 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER1)
              .replyComment("Hope you had a great time!")
              .createdAt(formatter.parse("01/02/2021").getTime())
              .review(SeedReviews.REVIEW5)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  // Demo Replies
  public static final ReplyEntity REPLY6;

  static {
    try {
      REPLY6 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER2)
              .replyComment("Thanks for visiting our locations!")
              .createdAt(formatter.parse("01/02/2021").getTime())
              .review(SeedReviews.REVIEW6)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final ReplyEntity REPLY7;

  static {
    try {
      REPLY7 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER2)
              .replyComment("Please let us know how we could improve!")
              .createdAt(formatter.parse("05/12/2019").getTime())
              .review(SeedReviews.REVIEW9)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final ReplyEntity REPLY8;

  static {
    try {
      REPLY8 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER2)
              .replyComment("Hope to see you again soon!")
              .createdAt(formatter.parse("01/06/2021").getTime())
              .review(SeedReviews.REVIEW15)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final ReplyEntity REPLY9;

  static {
    try {
      REPLY9 =
          ReplyEntity.builder()
              .replyUser(SeedUsers.USER2)
              .replyComment("We are sorry you did not enjoy our location.")
              .createdAt(formatter.parse("10/01/2020").getTime())
              .review(SeedReviews.REVIEW19)
              .build();
    } catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  public static final List<ReplyEntity> REPLIES =
      List.of(REPLY1, REPLY2, REPLY3, REPLY4, REPLY5, REPLY6, REPLY7, REPLY8, REPLY9);

  private SeedReplies() {
    throw new IllegalStateException("Utility class");
  }
}
