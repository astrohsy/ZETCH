package io.zetch.app.seed;

import io.zetch.app.domain.user.Affiliation;
import io.zetch.app.domain.user.UserEntity;
import java.util.List;

public class SeedUsers {
  private static final UserEntity USER_1 =
      UserEntity.builder()
          .username("admin")
          .affiliation(Affiliation.ADMIN)
          .displayName("Admin.")
          .displayName(null)
          .build();
  private static final UserEntity USER_2 =
      UserEntity.builder()
          .username("bob")
          .affiliation(Affiliation.STUDENT)
          .displayName("Bob R.")
          .displayName("bob@me.com")
          .build();
  private static final UserEntity USER_3 =
      UserEntity.builder()
          .username("cat")
          .affiliation(Affiliation.STUDENT)
          .displayName("Kat F.")
          .displayName("kat@kat.com")
          .build();
  private static final UserEntity USER_4 =
      UserEntity.builder()
          .username("amy")
          .affiliation(Affiliation.FACULTY)
          .displayName("Amy T.")
          .displayName("amy@dog.com")
          .build();
  private static final UserEntity USER_5 =
      UserEntity.builder()
          .username("sam")
          .affiliation(Affiliation.OTHER)
          .displayName("Sam 2.")
          .displayName("sam@from.com")
          .build();

  public static final List<UserEntity> USERS = List.of(USER_1, USER_2, USER_3, USER_4, USER_5);
}
