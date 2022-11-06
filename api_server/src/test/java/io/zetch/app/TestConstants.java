package io.zetch.app;

import io.zetch.app.domain.location.LocationEntity;
import io.zetch.app.domain.location.Type;
import io.zetch.app.domain.reply.ReplyEntity;
import io.zetch.app.domain.review.ReviewEntity;
import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import java.util.Date;

public class TestConstants {
  public static Long CREATED_BY = new Date("09/05/2022").getTime();
  public static Long USER_ID_1 = 100L;
  public static Long USER_ID_2 = 200L;
  public static Long REVIEW_ID_1 = 101L;
  public static Long REVIEW_ID_2 = 201L;
  public static Long REPLY_ID_1 = 102L;
  public static Long REPLY_ID_2 = 202L;

  public static final String USERNAME_1 = "bob";
  public static final String USERNAME_2 = "cat";
  public static final String DISPLAY_NAME_1 = "Bob";
  public static final String DISPLAY_NAME_2 = "Cat";
  public static final String LOCATION_NAME_1 = "Acme Restaurant";
  public static final String LOCATION_NAME_2 = "Foo Museum";
  public static final String EMAIL_1 = "bob@example.com";
  public static final String EMAIL_2 = "cat@example.com";
  public static final String COMMENT_1 = "It was a great experience";
  public static final String COMMENT_2 = "I did not like it";
  public static final String ADDRESS_1 = "155 Claremont NY";
  public static final String ADDRESS_2 = "120 Broadway ";

  public static UserEntity USER_1 =
      UserEntity.builder()
          .username(USERNAME_1)
          .displayName(DISPLAY_NAME_1)
          .email(EMAIL_1)
          .affiliation(Affiliation.STUDENT)
          .build();

  public static UserEntity USER_2 =
      UserEntity.builder()
          .username(USERNAME_2)
          .displayName(DISPLAY_NAME_2)
          .email(EMAIL_2)
          .affiliation(Affiliation.STUDENT)
          .build();

  public static LocationEntity LOCATION_1 =
      LocationEntity.builder()
          .name(LOCATION_NAME_1)
          .address(ADDRESS_1)
          .type(Type.RESTAURANT)
          .build();
  public static ReviewEntity REVIEW_1 =
      ReviewEntity.builder().comment(COMMENT_1).rating(3).user(USER_1).location(LOCATION_1).build();
  public static ReplyEntity REPLY_1 =
      ReplyEntity.builder()
          .replyUser(USER_1)
          .replyComment(COMMENT_1)
          .createdAt(CREATED_BY)
          .review(REVIEW_1)
          .build();
  public static ReplyEntity REPLY_2 =
      ReplyEntity.builder()
          .replyUser(USER_1)
          .replyComment(COMMENT_2)
          .createdAt(CREATED_BY)
          .review(REVIEW_1)
          .build();
  public static ReplyEntity REPLY_3 =
      ReplyEntity.builder()
          .replyUser(USER_2)
          .replyComment(COMMENT_2)
          .createdAt(CREATED_BY)
          .review(REVIEW_1)
          .build();
  public static LocationEntity LOCATION_2 =
      LocationEntity.builder().name(LOCATION_NAME_2).address(ADDRESS_2).build();
  public static ReviewEntity REVIEW_2 =
      ReviewEntity.builder().comment(COMMENT_2).rating(3).user(USER_2).location(LOCATION_2).build();
}
